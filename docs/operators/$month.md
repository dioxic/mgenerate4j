Returns the month from the input date.

### Options

- `date` (required) Input date.
- `type` (optional) The output display type (`NUMERIC`, `LONG_TEXT` or `SHORT_TEXT`). Default `NUMERIC`.
- `locale` (optional) The display locale. Defaults to `Locale.getDefault()`.

### Example

#### Template
```json
{
    "someDate": "$dt",
    "num": {
        "$month": "${someDate}"
    },
    "text": {
        "$month": {
            "date": "${someDate}",
            "type": "LONG_TEXT",
            "locale": {
                "language": "de",
                "country": "DE"
            }
        }
    }
}
```
#### Output
```json
{ "someDate": { "$date": "1990-06-05T16:08:39.137Z" }, "num": 6, "text": "Juni"}
```