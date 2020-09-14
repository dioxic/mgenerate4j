# Usage

## Standalone

```
Usage: cli [OPTIONS] COMMAND [ARGS]...

Options:
  -h, --help  Show this message and exit

Commands:
  generate  Generate data and output to a file or stdout
  load      Load data directly into MongoDB
  update    Update data in MongoDB
  delete    Delete data in MongoDB
  sample    Sample data in MongoDB
```

```
java -jar mgenerate.jar load template.json
```

## Library

```java
MongoClientSettings mcs = MongoClientSettings.builder()
        .codecRegistry(CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(new TemplateCodec()), MgenDocumentCodec.getCodecRegistry()))
        .build();

MongoCollection<Template> collection = MongoClients.create(mcs)
        .getDatabase("test")
        .getCollection("mgen", Template.class);

Template template = Template.from("c:\\tmp\\mongo.json");

collection.insertOne(template);
collection.insertOne(template);
collection.insertOne(template);
```

This will result in 3 _different_ documents being inserted as template resolution is performed during encoding.