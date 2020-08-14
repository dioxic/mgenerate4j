Returns a random country (or country code).

## Options

- `code` (optional) Return just the country code. Default `false`.

## Example

=== "Template"
    ```json
    {
        "country": { "$country": false }
    }
    ```
=== "Output"
    ```json
    {
        "country": "Paraguay"
    }
    ```