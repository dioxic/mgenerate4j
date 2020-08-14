Optionally returns an input value based on a boolean flag or a probability.
All operators support [optionality][optionality] so this operator is only necessary when dealing with constant values.

## Options

- `value` (required) Input value.
- `isNull` (optional) Is null or probability of null value. Accepts boolean or numeric between 0 and 1. Default `false`.

## Example

=== "Template"
    ```json
    { "nullValue": { "$optional": { "value": 123, "isNull": true } } }
    { "value": { "$optional": { "value": 123, "isNull": 0.5 } } }
    ```
=== "Output"
    ```json
    { }
    { "value": 123 }
    ```
    
    
[optionality]:       ../operators.md#optionality