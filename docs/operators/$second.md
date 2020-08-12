Returns the second from the input date.

### Options

- `date` (optional) Input date. Default random (0-59).

### Example

#### Template
```json
{
    "someDate": "$dt",
    "second": { "$second": "${someDate}" }
}
```
#### Output
```json
{
    "someDate": { "$date": "2016-06-28T15:28:54.721Z" } ,
    "second": 54
}
```