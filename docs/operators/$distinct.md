Returns the distinct set of the input array.

## Options

- `values` (required) Input array.

## Example

=== "Template"
    ```json
    {
        "distinct": {
            "$distinct": ["badger", "badger", "halibut"]
        }
    }
    ```
=== "Output"
    ```json
    {
        "distinct": ["badger", "halibut"]
    }
    ```