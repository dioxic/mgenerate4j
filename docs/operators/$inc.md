Increments the `input` value by the `step` argument.

**Aliases:** `$inc`, `$increment`

## Options

- `input` (required) Input value. Must be numeric.
- `step` (optional) Value to increment by. Default `1`.

## Example

=== "Template"
    ```json
    {
        "n": "$number",
        "nPlus2": {
            "$inc": {
                "input": "${n}",
                "step": 2
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "n": 100,
        "nPlus2": 102
    }
    ```