Generates a random email address.

## Options

- `username` (optional) Email prefix.
- `domain` (optional) Email domain name.

## Example

=== "Template"
    ```json
    {
        "email": {
            "$email": {
                "domain": "gmail.com"
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "email": "emmett.walsh@gmail.com"
    }
    ```