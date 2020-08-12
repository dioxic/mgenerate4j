Returns the day of the week from the input date.

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
        "$dayOfWeek": "${someDate}"
    },
    "text": {
        "$dayOfWeek": {
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
{ "someDate": { "$date": "1998-01-05T14:55:17.17Z" }, "num": 1, "text": "Montag" }
```