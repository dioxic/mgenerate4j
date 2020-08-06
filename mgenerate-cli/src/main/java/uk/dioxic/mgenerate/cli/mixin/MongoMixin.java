package uk.dioxic.mgenerate.cli.mixin;

import com.mongodb.*;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoMixin {

    @Getter
    @Option(names = {"-c", "--collection"},
            description = "collection to use",
            required = true,
            paramLabel = "arg")
    private String collectionName;

    @Getter
    @ArgGroup(heading = "%nAuthentication Options:%n", exclusive = false)
    private AuthenticationOptions authenticationOptions;

    @Getter
    @ArgGroup(heading = "%nConnection Options:%n", exclusive = false)
    private ConnectionOptions connectionOptions = new ConnectionOptions();

    private static class ConnectionOptions {
        @Getter
        @Option(names = {"-d", "--database"},
                description = "database to use (default: ${DEFAULT-VALUE})",
                defaultValue = "test",
                paramLabel = "arg")
        private String databaseName;

        @Option(names = {"--host"},
                description = "server to connect to (default: ${DEFAULT-VALUE})",
                defaultValue = "localhost",
                paramLabel = "arg")
        private String host;

        @Option(names = {"--port"},
                description = "port to connect to (default: ${DEFAULT-VALUE})",
                defaultValue = "27017",
                paramLabel = "arg")
        private Integer port;

        @Option(names = {"--tls"},
                description = "use TLS for connections (default: ${DEFAULT-VALUE})")
        private Boolean tls;

        @Option(names = {"--tlsAllowInvalidHostnames"},
                description = "allow connections to servers with non-matching hostnames (default: ${DEFAULT-VALUE})",
                defaultValue = "false")
        private Boolean allowInvalidHostnames;

        @Option(names = {"--uri"},
                description = "mongo URI (default: ${DEFAULT-VALUE})",
                defaultValue = "mongodb://localhost:27017/test",
                paramLabel = "arg")
        private String uri;

        void apply(MongoClientSettings.Builder mcsBuilder) {
            if (uri != null) {
                mcsBuilder.applyConnectionString(new ConnectionString(uri));
            } else {
                mcsBuilder.applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(host, port))));
            }

            mcsBuilder.applyToSslSettings(builder -> {
                if (tls != null) {
                    builder.enabled(tls);
                }
                if (allowInvalidHostnames != null) {
                    builder.invalidHostNameAllowed(allowInvalidHostnames);
                }
            });
        }
    }

    private static class AuthenticationOptions {
        private enum AuthMechanism {GSSAPI, X509, SCRAMSHA, LDAP}

        @Option(names = {"-u", "--username"},
                description = "username for authentication",
                required = true,
                paramLabel = "arg")
        private String username;

        @Option(names = {"-p", "--password"},
                description = "password for authentication",
                interactive = true,
                required = true,
                arity = "0..1",
                paramLabel = "arg")
        private char[] password;

        @Option(names = {"--authenticationDatabase"},
                description = "user source (default: ${DEFAULT-VALUE})",
                required = true,
                defaultValue = "admin",
                paramLabel = "arg")
        private String authenticationDatabase;

        @Option(names = {"--authenticationMechanism"},
                description = "authentication mechanism, one of ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})",
                required = true,
                defaultValue = "SCRAMSHA",
                paramLabel = "arg")
        private AuthMechanism authenticationMechanism;

        void apply(MongoClientSettings.Builder mcsBuilder) {
            if (hasCredential()) {
                mcsBuilder.credential(MongoCredential.createCredential(username, authenticationDatabase, password));
            }
        }

        boolean hasCredential() {
            return username != null && password != null && authenticationDatabase != null;
        }
    }

    private MongoClient mongoClient;

    private final List<Function<MongoClientSettings.Builder, MongoClientSettings.Builder>> clientSettingFunctions = new ArrayList<>();

    private CodecRegistry codecRegistry;

    public MongoClient getClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(getMongoClientSettings());
        }

        mongoClient.getDatabase("test")
                .getCollection("collection")
                .withReadPreference(ReadPreference.primaryPreferred())
                .withReadConcern(ReadConcern.MAJORITY)
                .find(Filters.eq("a", "a"));

        return mongoClient;
    }

    private MongoClientSettings getMongoClientSettings() {
        MongoClientSettings.Builder mcsBuilder = MongoClientSettings.builder();

        mcsBuilder.applicationName("mgenerate4j");
        connectionOptions.apply(mcsBuilder);

        if (authenticationOptions != null) {
            authenticationOptions.apply(mcsBuilder);
        }

        if (codecRegistry != null) {
            mcsBuilder.codecRegistry(codecRegistry);
        }

        // TODO - put in cli options
        mcsBuilder.readConcern(ReadConcern.MAJORITY);
        mcsBuilder.writeConcern(WriteConcern.MAJORITY);
        mcsBuilder.retryWrites(true);

        clientSettingFunctions.forEach(func -> func.apply(mcsBuilder));

        return mcsBuilder.build();
    }

    public void applyToClientSettings(Function<MongoClientSettings.Builder, MongoClientSettings.Builder> builderFunction) {
        clientSettingFunctions.add(builderFunction);
    }

    public void addCodecRegistry(CodecRegistry codecRegistry) {
        this.codecRegistry = fromRegistries(codecRegistry, MongoClientSettings.getDefaultCodecRegistry());
    }

    public MongoDatabase getDatabase() {
        return getDatabase(connectionOptions.getDatabaseName());
    }

    public MongoDatabase getDatabase(String database) {
        return getClient().getDatabase(database);
    }

    public <TDocument> MongoCollection<TDocument> getCollection(Class<TDocument> clazz) {
        return getDatabase(connectionOptions.getDatabaseName()).getCollection(getCollectionName(), clazz);
    }

    public MongoCollection<Document> getCollection() {
        return getCollection(Document.class);
    }

}
