# Custom Operators

If the out-of-the-box operators don't meet your needs, you can create your own.

## Setup

mgenerate4j uses compile-time annotation processing so you will need to enable this.

=== "Maven"
    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>uk.dioxic.mgenerate</groupId>
                            <artifactId>mgenerate-apt</artifactId>
                            <version>0.0.5</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```
    
=== "Gradle"
    ```groovy
    dependencies {
        annotationProcessor 'uk.dioxic.mgenerate:mgenerate-apt:0.0.5'
        compile 'uk.dioxic.mgenerate:mgenerate-core:0.0.5'
    }
    ```
    
=== "Gradle KT"
    ```kotlin
    dependencies {
        annotationProcessor("uk.dioxic.mgenerate:mgenerate-apt:0.0.5")
        compile("uk.dioxic.mgenerate:mgenerate-core:0.0.5")
    }
    ```

## Basics

Creating a new operator is easy, in 3 simple steps:

1. Create a new class
2. Implement the `Resolvable` interface or extend the `AbstractOperator` class
3. Annotate with `@Operator`

```java
@Operator
public class Hello extends AbstractOperator<String> {

    @Override
    public String resolveInternal() {
      return "hello world!";
    }
}
```

The annotation processor will automatically create a builder class for your operator at compile-time in the same package.

You then add the builder to the `OperatorFactory` by either giving it the builder class or it's package.

```java
public class Main {
    static {
        // add single operator builder
        OperatorFactory.addBuilder(HelloBuilder.class);

        // add all operator builders under this package signature
        OperatorFactory.addBuilder("com.example");
    }
}
```

Your custom operator can then be used just like any other operator.

=== "Template"
    ```json
    { 
        "greeting": "$hello"
    }
    ```

=== "Output"
    ```json
    {
        "greeting": "hello world!"
    }
    ```

## Aliasing

By default, the operator name will be the same as the lower-case class name but it can be given one or more aliases
by specifying the value attribute on the `@Operator` annotation.

```java
@Operator({"greet", "sayhello"})
public class Hello extends AbstractOperator<String> {

    @Override
    public String resolveInternal() {
      return "hello world!";
    }
}
```

=== "Template"
    ```json
    { 
        "greeting": "$hello",
        "greeting2": "$greeting",
        "greeting3": "$sayhello"
    }
    ```

=== "Output"
    ```json
    {
        "greeting": "hello world!",
        "greeting2": "hello world!",
        "greeting3": "hello world!"
    }
    ```

## Arguments

If you want to pass arguments into your operator you can annotation either a class-level variable or method with
the `@OperatorProperty` annotation. These will need to be either package-protected or public.

If the property is required, set the required attribute on the annotation.

```java
@Operator
public class Greet extends AbstractOperator<String> {

    @OperatorProperty(required = true)
    String name;

    @Override
    public String resolveInternal() {
        return "hello " + name + "!";
    }
}
```

=== "Template"
    ```json
    { "greeting": { "$greet": "Bob" } }
    ```
=== "Output"
    ```json
    { "greeting": "hello Bob!" }
    ```

    ```

### Dynamic Arguments

We can pass another operator into our `customGreeting` property and this will work fine, however, in this mode,
the input operator will be hydrated only once. So, every document generated will have the same value.

=== "Template"
    ```json
    { "greeting": { "$greet": "$first" } }
    ```
=== "Output"
    ```json
    { "greeting": "hello Bob!" }
    { "greeting": "hello Bob!" }
    { "greeting": "hello Bob!" }
    ```

If we want the input operator to be resolved separately for every generated document then we need the input variable
to be a `Resolvable` type and resolve it in our `resolveInternal` method.

```java
@Operator
public class Greet extends AbstractOperator<String> {

    @OperatorProperty(required = true)
    Resolvable<String> name;

    @Override
    public String resolveInternal() {
        return "hello " + name.resolve() + "!";
    }
}
```

=== "Template"
    ```json
    { "greeting": { "$greet": "$first" } }
    ```
=== "Output"
    ```json
    { "greeting": "hello Bob!" }
    { "greeting": "hello Mike!" }
    { "greeting": "hello Stavros!" }
    ```

### Default Values

We can set defaults for our operator properties. If we are using `Resolvable` types then we need to wrap them using the `Wrapper` helper.

```java
@Operator
public class Greet extends AbstractOperator<String> {

    @OperatorProperty
    String greeting = "hello";

    @OperatorProperty
    Resolvable<String> name = Wrapper.wrap("Bob");

    @Override
    public String resolveInternal() {
      return greeting + " " + name.resolve() + "!";
    }
}
```

=== "Template"
    ```json
    { "greeting": "$greet"
    { "greeting": { "$greet": { "greeting": "bonjour", "name": "Stavros" } } }
    ```
=== "Output"
    ```json
    { "greeting": "hello Bob!" }
    { "greeting": "bonjour Stavros!" }
    ```