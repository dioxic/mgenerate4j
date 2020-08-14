Takes an array and a number `element` and returns the `element`-th value of
the array. `element` is zero-based (`0` returns the first element).

## Options

- `array` (required) Array of values or operators to choose from.
- `element` (optional) Index of the array element to pick. Default `0`.

## Example

=== "Template"
    ```json
    {
        "color": {
            "$pick": {
                "array": ["green", "red", "blue"],
                "element": 1
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "color": "red"
    }
    ```