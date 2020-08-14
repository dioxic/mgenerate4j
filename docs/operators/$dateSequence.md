Returns a sequence of dates from an initial start date.

**Aliases:** `$dateSeq`, `$dateSequence`

## Options

- `start` (optional) Start date. Default `NOW`.
- `step` (optional) The step increment. Default `1`.
- `chronoUnit` (optional) The chonological unit to increment by (`YEARS`,`MONTHS`,`WEEKS`,`DAYS`,`HOURS`,`SECONDS`,`MILLIS`)

## Example

=== "Template"
    ```json
    {
        "seq": {
            "$dateSeq": {
                "start": "2020-01-01",
                "chronoUnit": "MINUTES",
                "step": 2
            }
        }
    }
    ```
=== "Output"
    ```json
    { "seq": { "$date": "2020-01-01T00:00:00Z" } }
    { "seq": { "$date": "2020-01-01T00:02:00Z" } }
    { "seq": { "$date": "2020-01-01T00:04:00Z" } }
    { "seq": { "$date": "2020-01-01T00:06:00Z" } }
    { "seq": { "$date": "2020-01-01T00:08:00Z" } }
    ...
    ```