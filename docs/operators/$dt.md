Returns a random date object, optionally between specified `min` and `max` values.
If `min` and/or `max` are provided, they need to be in ISO-8601 format.

**Aliases:** `$dt`, `$dateTime`, `$mgDate`

## Options

- `min` (optional) Minimum date.
- `max` (optional) Maximum date.

## Example

=== "Template"
    ```json
    {
        "last_login": {
            "$dt": {
                "min": "2015-01-01",
                "max": "2016-12-31T23:59:59.999Z"
            }
        }
    }
    ```
=== "Extended JSON Output"
    ```json
    {
        "last_login": {
            "$date": "2016-06-28T15:28:54.721Z"
        }
    }
    ```
=== "Shell Output"
    ```json
    {
        "last_login": ISODate("2016-06-28T15:28:54.721Z")
    }
    ```