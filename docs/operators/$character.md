Returns a random character.

If no arguments are set, the pool defaults to all alpha, numeric and some symbols.

## Options

- `pool` (optional, primary) Character pool to construct string from. Overrides other arguments.
- `alpha` (optional) Include alpha characters in the character pool. Defaults to `false`.
- `numeric` (optional) Include numeric characters in the character pool. Defaults to `false`.
- `symbols` (optional) Include symbols characters in the character pool. Defaults to `false`.
- `casing` (optional) Set the casing of the alpha characters (`UPPER` or `LOWER`). Default includes both cases.

## Example

=== "Template"
    ```json
    {
        "charAlpha": { "$character" : { "alpha": true, "casing": "upper" } },
        "charFromPool": { "$character" : "MONGO" }
    }
    ```
=== "Output"
    ```json
    {
        "charAlpha": "Z",
        "charFromPool": "M"
    }
    ```