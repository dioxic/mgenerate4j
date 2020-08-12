# Operators

Operators are functions which generate particular data. They are denoted by a `$` prefix and can be used in
various formats.

Operators can have input arguments that allow modification of their output. Sometimes these are
mandatory, sometimes default values exist. When they exist, default values are vaguely sensible.

## Syntax

If an operator has no required arguments, or the required arguments have default values, then the simple string format can be used:

```json
{ "key": "$operator" }
```

When there is an argument marked as `primary` or only a single required argument then the short object format can be used:

```json
{ "key": { "$operator": 123 } }
```

If multiple arguments need to be specified the long object format is used:

```json
{ "key": { "$operator": { "argument1": 123, "argument2": "xyz" } } }
```

## Chaining

An operator can be passed as an argument to another operator allowing multiple operators to be chained together. 
 
Here we pass in a random number between 0 and 5 to the `number` option
of the `$array` operator to generate variable-length arrays.

=== "Template"
    ```json
    {
        "names": {
            "$array": {
                "of": "$first",
                "number": {
                    "$integer": { "min": 0, "max": 5 }
                }
            }
        }
    }
    ```
=== "Output"
    ```json
    { "name": ["Tina"] }
    { "name": ["Tito", "Andre", "Telly", "Stavros"] }
    { "name": ["Bob"] }
    { "name": [] }
    { "name": ["Andre", "Monserrate", "Earl"] }
    ```

## Optionality

All out-of-the-box operators support optionality by overriding the `isNull` option and providing either a boolean value or a
probability between 0 and 1. If this resolves to true, the operator is excluded from the result.

[Custom operators][custom-operators] will also exhibit this behaviour if they extend the `AbstractOperator` class.

There is also an [$optional][$optional] operator to support optionality for constant values.

=== "Template"
    ```json
    { "nullValue1": { "$number": { "isNull": true } } }
    { "nullValue2": { "$first": { "isNull": 1 } } }
    { "optionalValue": { "$first": { "isNull": 0.5 } } }
    { "includedValue1": { "$first": { "isNull": 0 } } }
    { "includedValue2": { "$number": { "isNull": false } } }
    ```
=== "Output"
    ```json
    { }
    { }
    { "optionalValue": "Bob" }
    { "includedValue1": "Alice" }
    { "includedValue2": 2399829 }
    ```
    
[$optional]:         TODO
[custom-operators]:  custom-operators.md