Returns a random floating point number (64-bit).

**Aliases:** `$floating`, `$float`, `$double`, `$mgNumberDouble`

## Options

- `min` (optional) minimum value. Default `-2^1074`.
- `max` (optional) maximum value. Default `2^1023`.

## Example

=== "Template"
    ```json
    {
        "price": {
            "$double": {
                "min": 100
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "price": 7647.42134652
    }
    ```