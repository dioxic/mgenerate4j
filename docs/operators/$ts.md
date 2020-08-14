Returns a random BSON Timestamp.

**Aliases:** `$ts`, `$mgTimestamp`

## Options

- `t` (optional) Seconds since epoch. Default random.
- `i` (optional) Increment value. Default random.

## Example

=== "Template"
    ```json
    {
        "ts": {
            "$ts": {
                "t": "$int",
                "i": 20
            }
        }
    }
    ```
=== "Output"
    ```json
    {
        "ts": Timestamp(1597439758, 20)
    }
    ```