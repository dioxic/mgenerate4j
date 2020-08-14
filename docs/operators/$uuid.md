Returns a random UUID.

## Options

- `type` (optional) The data type of the UUID (`BINARY` or `STRING`). Default `BINARY`.

## Example

=== "Template"
    ```json
    {
        "uuid": "$uuid"
    }
    {
        "stringUuid": { "$uuid": "STRING" }
    }
    ```
=== "Extended JSON Output"
    ```json
    {
        "uuid": {
            "$binary": {
                "base64": "2dFU/OjFRZac1SJzI1PDXw==",
                "subType": "04"
            }
        }
    }
    {
        "stringUuid": "606340f0-fceb-419b-91c5-fdf192d08fad"
    }
    ```
=== "Compass/Shell Output"
    ```json
    {
        "uuid": UUID("ee780ef4-362f-45ca-a816-55120e0afe18")
    }
    {
        "stringUuid": "606340f0-fceb-419b-91c5-fdf192d08fad"
    }
    ```