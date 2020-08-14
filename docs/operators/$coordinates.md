Returns a 2-element array of longitude/latitude coordinates, optionally within
`longBounds` and/or `latBounds` bounds.

**Aliases:** `$coord`, `$coordinates`

## Options

- `longBounds` (optional) Array of longitude bounds. Default `[-180, 180]`.
- `latBounds` (optional) Array of latitude bounds. Default `[-90, 90]`.

## Example

=== "Template"
    ```json
    {
        "position": {
            "$coordinates": { "longBounds": [-20, -19] }
        }
    }
    ```
=== "Output"
    ```json
    {
        "position": [-19.96851, -47.46141]
    }
    ```