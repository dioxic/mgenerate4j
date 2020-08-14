Returns a sensible age for a human.

## Options

- `type` (optional) Demographic. One of (`CHILD`, `TEEN`, `ADULT`, `SENIOR`, `DEFAULT`).
    
## Example

=== "Template"
    ```json
    {
        "age": { "$age": "ADULT" }
    }
    ```
=== "Output"
    ```json
    {
        "age": 43
    }
    ```