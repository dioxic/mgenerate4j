Chooses one element from an array of possible choices with uniform probability.
Optionally chooses with probability proportional to a provided `weights` array.

## Options

- `domain` (optional) Domain name for url.
- `path` (optional) Path for url.
- `extension` (optional) include file extension (`true`/`false`).

## Example

=== "Template"
    ```json
    {
        "url": {
            "$url": {
                "domain": "mongodb.org"
                "extension": true
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "url": "http://mongodb.org/eveniet/quaerat.css"
    }
    ```