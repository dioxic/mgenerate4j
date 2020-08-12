Returns the year from the input date.

### Options

- `date` (optional) Input date. Defaults to now.
- `unit` (optional) ChronoUnit to for epoch (e.g `MILLIS`, `SECONDS`, `MINUTES`). Defaults to `MILLIS`.

### Example

#### Template
```json
{
    "nowEpoch": "$epoch",
    "someDate": "$dt",
    "someEpoch": {
        "$epoch": {
            "date": "${someDate}",
            "unit": "SECONDS"
        }
    }
}
```
#### Output
```json
{ "nowEpoch": 1569089885601, "someDate": { "$date": "1997-02-25T18:30:36.731Z" }, "someEpoch": 1569089885 }
```