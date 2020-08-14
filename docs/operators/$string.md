Returns a random string.

## Options

- `length` (optional) Length of output string. Default `5`
- `pool` (optional) Character pool to construct string from. Defaults to alpha, numeric and some symbols.

## Example

=== "Template"
    ```json
    {
        "string": {
            "$string": {
                "length": 20,
                "pool": "MONGO40*"
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "string": "*44NGOMGN*ONOGOO*4O0"
    }
    ```