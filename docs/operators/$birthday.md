Returns a sensible birthday for a human.

## Options

- `type` (optional) Demographic. One of (`CHILD`, `TEEN`, `ADULT`, `SENIOR`, `DEFAULT`). 

## Example

=== "Template"
    ```json
    {
        "birthday": { "$birthday": "ADULT" }
    }
    ```
=== "Output"
    ```json
    {
        "birthday": ISODate("1985-08-15T22:21:35.343Z")
    }
    ```