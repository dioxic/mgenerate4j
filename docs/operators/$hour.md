Returns the hour from the input date.

## Options

- `date` (optional) Input date. Default random (0-23).

## Example

=== "Template"
    ```json
    {
        "someDate": "$dt",
        "hour": { "$hour": "${someDate}" }
    }
    ```
=== "Output"
    ```json
    {
        "someDate": ISODate("2016-06-28T15:28:54.721Z"),
        "hour": 15
    }
    ```