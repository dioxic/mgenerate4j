Returns the day of the month from the input date.

### Options

- `date` (required) Input date.

### Example

#### Template
```json
{
    "someDate": "$dt",
    "dayOfMonth": { "$dayOfMonth": "${someDate}" }
}
```
#### Output
```json
{
    "someDate": { "$date": "2016-06-28T15:28:54.721Z" } ,
    "dayOfMonth": 28
}
```