Returns a sequence of 64-bit integers.

**Aliases:** `$longSeq`, `$longSequence`

## Options

- `start` (optional) Initial value. Default `0`.
- `step` (optional) The step increment. Default `1`.

## Example

=== "Template"
    ```json
    {
        "seq": {
            "$longSeq" : {
                "start": 2147483647
                "step": 2
            }
        }
    }
    ```
=== "Extended JSON Output"
    ```json
    { "seq": { "$numberLong": "2147483647" } }
    { "seq": { "$numberLong": "2147483649" } }
    { "seq": { "$numberLong": "2147483651" } }
    { "seq": { "$numberLong": "2147483653" } }
    { "seq": { "$numberLong": "2147483655" } }
    ...
    ```
=== "Shell Output"
    ```json
    { "seq": NumberLong("2147483647") }
    { "seq": NumberLong("2147483649") }
    { "seq": NumberLong("2147483651") }
    { "seq": NumberLong("2147483653") }
    { "seq": NumberLong("2147483655") }
    ...
    ```