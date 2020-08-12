# Usage

## Standalone

```
Usage: mgenerate [-h] [--debug] [-n=<number>] [-o=<output>] TEMPLATE
      TEMPLATE            template file path
      --debug             debug logging
  -h, --help              display a help message
  -n, --number=<number>   number of documents to generate (default: 10)
  -o, --out=<output>      output file path
```

```
java -jar mgenerate.jar template.json
```

## Library

```java
MongoClientSettings mcs = MongoClientSettings.builder()
        .codecRegistry(MgenDocumentCodec.getCodecRegistry())
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