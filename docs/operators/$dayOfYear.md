Returns the day of the year from the input date.

## Options

- `date` (required) Input date.

## Example

=== "Template"
    ```json
    {
        "someDate": "$dt",
        "day": {
            "$dayOfYear": "${someDate}"
        }
    }
    ```
=== "Output"
    ```json
    {
        "someDate": { "$date": "2006-05-31T07:41:09.474Z" },
        "day": 151
    }
    ```