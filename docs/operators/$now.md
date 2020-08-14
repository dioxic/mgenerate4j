Returns the BSON datetime at the point the operator is hydrated.

## Example

=== "Template"
    ```json
    { "created": "$now" }
    ```
=== "Extended JSON Output"
    ```json
    { "created": { "$date": "2017-02-20T04:44:24.880Z" } }
    ```
=== "Mongo Shell JSON Output"
    ```json
    { "created": ISODate("2017-02-20T04:44:24.880Z") }
    ```