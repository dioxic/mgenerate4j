# mgenerate4j [![travis][travis_img]][travis_url] [![maven][maven_img]][maven_url]

_mgenerate4j_ is a tool for generating JSON documents from a template. It can be used as a standalone application or
 as a library.
 
Custom codecs are included so templates will work natively with the MongoDB Java drivers.
 
The core syntax is largely the same as [mgeneratejs](https://github.com/rueckstiess/mgeneratejs) by Thomas Rueckstiess.

See the [Wiki](https://github.com/dioxic/mgenerate4j/wiki) for details.

## Usage

### Standalone

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

### Library

```java
MongoClientSettings mcs = MongoClientSettings.builder()
        .codecRegistry(TemplateCodec.getCodeRegistry())
        .build();

Template template = Template.from("c:\\tmp\\mongo.json");
MongoCollection<Template> collection = MongoClients.create(mcs)
        .getDatabase("test")
        .getCollection("mgen", Template.class);

collection.insertOne(template);
collection.insertOne(template);
collection.insertOne(template);
```

This will result in 3 _different_ documents being inserted as template resolution is performed during encoding.

## Example

Template:
```json
{ "name": "$name", "age": "$age", "emails": { "$array": { "of": "$email", "number": 2} } }
```

Results:
```json
{"name": "Jeffery Dooley", "age": 71, "emails": ["daisy.monahan@hotmail.com", "jacey.bauch@hotmail.com"]}
{"name": "Durward Morar", "age": 3, "emails": ["osborne.kassulke@hotmail.com", "amparo.stokes@gmail.com"]}
{"name": "Reyes Cartwright", "age": 42, "emails": ["candida.macejkovic@hotmail.com", "kasey.vandervort@yahoo.com"]}
{"name": "Naomi Nicolas", "age": 117, "emails": ["ottilie.murazik@gmail.com", "dillon.marvin@hotmail.com"]}
{"name": "Athena Buckridge", "age": 36, "emails": ["aryanna.tromp@gmail.com", "celestino.buckridge@gmail.com"]}
```

## License

Apache 2.0

[regexp]: https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/RegExp
[bson-spec]: http://bsonspec.org/spec.html
[date-parse]: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/parse
[travis_img]: https://api.travis-ci.org/dioxic/mgenerate4j.svg?branch=master
[travis_url]: https://travis-ci.org/dioxic/mgenerate4j
[maven_img]: https://img.shields.io/maven-central/v/uk.dioxic.mgenerate/mgenerate-parent
[maven_url]: https://search.maven.org/search?q=g:uk.dioxic.mgenerate
