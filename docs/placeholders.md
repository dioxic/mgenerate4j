# Placeholders

There are two types of placeholder; document placeholders and faker placeholders.

Document placeholders are used when we want to refer to a value in the generated document.

Faker placeholders are used when we want to refer to a faker key that is not directly exposed by an operator or
when we want to combine multiple faker keys together.

## Document Placeholders

The basic syntax for document placeholders is `${ <document key> }`.

=== "Template"
    ```json
    {
        "firstName": "$first",
        "lastName": "$last",
        "fullName": "${firstName} ${lastName}"
    }
    ```
=== "Output"
    ```json
    {
        "firstName": "Jacques",
        "lastName": "Strap",
        "fullName": "Jacques Strap"
    }
    ```

### Emdedded Fields

Dot-notation is supported in document placeholders so, we can reference embedded fields:

=== "Template"
    ```json
    {
        "person": {
            "name": "$first",
            "age": 26 
        },
        "repeatName": "${person.name}"
    }
    ```
=== "Output"
    ```json
    {
        "person": {
            "name": "John",
            "age": 26
        },
        "repeatName": "John"
    }
    ```

### Arrays

Dot-notation is also supported for referencing array elements by their index.

*Note:* Arrays are zero-indexed in mgenerate

=== "Template"
    ```json
    {
        "names": ["Shaina", "Callie", "Maximilian", "Mackenzie", "Bob"],
        "element0": "${names.0}",
        "element2": "${names.2}"
    }
    ```
=== "Output"
    ```json
    {
        "names": ["Shaina", "Callie", "Maximilian", "Mackenzie", "Bob"],
        "element0": "Shaina",
        "element2": "Maximilian"
    }
    ```

If the placeholder refers to a field which exists more than once, an array will be returned:

=== "Template"
    ```json
    {
        "people": [
            { "name": "Shaina" },
            { "name": "Callie" },
            { "name": "Maximilian" }
        ],
        "names": "${people.name}"
    }
    ```
=== "Output"
    ```json
    {
        "people": [
            { "name": "Shaina" },
            { "name": "Callie" },
            { "name": "Maximilian" }
        ],
        "names": ["Shaina", "Callie", "Maximilian"]
    }
    ```

N-depth nesting with mixed array and document references will also work:

=== "Template"
    ```json
    {
        "people": [
            {
                "name": "Shaina",
                "addresses": [
                    { "city": "$city" },
                    { "city": "$city" }
                ]
            },
            {
                "name": "Callie",
                "addresses": { "city": "$city" }
            },
            {
                "name": "Maximilian"
            }
        ],
        "cities": "${people.addresses.city}"
    }
    ```
=== "Output"
    ```json
    {
        "people": [
            {
                "name": "Shaina",
                "addresses": [
                    { "city": "London" },
                    { "city": "Cardiff" } 
                ]
            },
            {
                "name": "Callie",
                "addresses": { "city": "New York" }
            },
            {
                "name": "Maximilian"
            }
        ],
        "cities": ["London", "Cardiff", "New York"]
    }
    ```

And, of course, document placeholders can be passed to an operator.

=== "Template"
    ```json
    {
        "scores": [
            { "type": "homework", "score": 54 },
            { "type": "exam", "score": 99 },
            { "type": "coursework", "score": 13 }
        ],
        "topScore": { "$max": "${scores.score}" }
    }
    ```
=== "Output"
    ```json
    {
        "scores": [
            { "type": "homework", "score": 54 },
            { "type": "exam", "score": 99 },
            { "type": "coursework", "score": 13 }
        ],
        "topScore": 99
    }
    ```

## Faker Placeholders

The syntax for document placeholders is `#{ <faker key> }`.

Faker keys can be found in the [faker library resources][faker4j].

=== "Template"
    ```json
    { "email": "#{name.first_name}.#{name.last_name}@github.com" }
    ```
=== "Output"
    ```json
    { "email": "Seymour.Butz@github.com" }
    ```
    
[extended JSON]:    https://github.com/mongodb/specifications/blob/master/source/extended-json.rst
[faker4j]:          https://github.com/dioxic/faker4j/tree/master/src/main/resources/locale
[$optional]:        TODO