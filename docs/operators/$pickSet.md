Takes an array and a number `quantity` and returns a new n-element array containing unique values from the input array.
Optionally picks with probability proportional to a provided `weights` array.

## Options

- `from` (required) Array of values or operators to choose from.
- `quantity` (optional) The size of the output array. Default `1`.
- `weights` (optional) Array of weightings (must be the same length as `array`).

## Example

=== "Template"
    ```json
    {
        "color": {
            "$pickSet": {
                "from": ["green", "red", "blue"],
                "quantity": 2
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "color": ["red", "green"]
    }
    ```