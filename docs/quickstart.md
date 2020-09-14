# Quickstart

Assuming git is installed:

```bash
$ git clone https://github.com/dioxic/mgenerate4j.git
$ ./gradlew shadowJar
$ java -jar mgenerate-cli/build/lib/mgenerate-cli-0.0.7-all.jar template.json
```

To import as a maven dependency:

```xml
<dependency>
    <groupId>uk.dioxic.mgenerate</groupId>
    <artifactId>mgenerate-core</artifactId>
    <version>0.0.7</version>
</dependency>
```

To import as a gradle dependency:
```groovy
dependencies {
    compile 'uk.dioxic.mgenerate:mgenerate-core:0.0.7'
}
```

**Note:** You can download the precompiled executable jar [here](https://github.com/dioxic/mgenerate4j/releases/download/v0.0.7/mgenerate.jar).