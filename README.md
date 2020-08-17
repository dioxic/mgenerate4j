<p align="center"><img src="docs/img/logo_text.png"></p>

[![build][build_badge]][build_url] [![docs][docs_badge]][gh-page] [![maven][maven_badge]][maven_url] ![licence][licence_badge]

**mgenerate4j** is a java library that makes generating test data for MongoDB easy.

Rich, representative test data can be generated from a template and loaded directly into MongoDB (or output to a file).

mgenerate4j can generate a wide variety of common data (e.g. names, addresses, emails, ip address) but is easy to extend if
you need to do generate something that isn't covered by the out-of-the-box functions. 

What does it look like? This is an example of a simple template:

```json
{
  "name": "$name",
  "age": "$age",
  "emails": {
    "$array": {
      "of": "$email",
      "number": 2
    } 
  }
}
``` 

And here's what the output might look like:

```json
{
  "name": "Jeffery Dooley",
  "age": 71,
  "emails": ["daisy.monahan@hotmail.com", "jacey.bauch@hotmail.com"]
}
```

To generate and load documents into MongoDB from a template, the CLI can be used:

```
$ java -jar mgenerate.jar load --uri "mongodb://localhost:27017" template.json
``` 

## Documentation

The full documentation for mgenerate4j can be found [here][gh-page] .

## Installation

mgenerate4j is distributed through [Maven Central][maven_url].

To import as a maven dependency:
```xml
<dependency>
    <groupId>uk.dioxic.mgenerate</groupId>
    <artifactId>mgenerate-core</artifactId>
    <version>0.0.6</version>
</dependency>
```

To import as a gradle dependency:
```groovy
dependencies {
    compile 'uk.dioxic.mgenerate:mgenerate-core:0.0.6'
}
```

**Precompiled executable jars are also available in [releases][releases].**

## License

Apache 2.0

[hack]:          http://hack
[bson-spec]:     http://bsonspec.org/spec.html
[docs_badge]:    https://github.com/dioxic/mgenerate4j/workflows/docs/badge.svg
[build_badge]:   https://github.com/dioxic/mgenerate4j/workflows/build/badge.svg
[maven_badge]:   https://img.shields.io/maven-central/v/uk.dioxic.mgenerate/mgenerate-parent
[travis_badge]:  https://api.travis-ci.org/dioxic/mgenerate4j.svg?branch=master
[licence_badge]: https://img.shields.io/hexpm/l/apa
[build_url]:     https://github.com/dioxic/mgenerate4j/actions?query=workflow%3A%22build%22
[travis_url]:    https://travis-ci.org/dioxic/mgenerate4j
[maven_url]:     https://search.maven.org/search?q=g:uk.dioxic.mgenerate
[releases]:      https://github.com/dioxic/mgenerate4j/releases/latest
[gh-page]:       https://dioxic.github.io/mgenerate4j/
