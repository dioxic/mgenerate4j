# Quickstart

Assuming git and [Maven](https://maven.apache.org/) is installed:

```bash
$ git clone https://github.com/dioxic/mgenerate4j.git
$ mvn clean package
$ java -jar mgenerate-core/target/mgenerate.jar example-template.json
```

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

**Note:** You can download the precompiled executable jar [here](https://github.com/dioxic/mgenerate4j/releases/download/v0.0.6/mgenerate.jar).