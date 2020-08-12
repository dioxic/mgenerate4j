Returns the year from the input date.

### Options

- `date` (optional) Input date. Default random (1900-2019).

### Example

#### Template
```json
{
    "someDate": "$dt",
    "year": { "$year": "${someDate}" }
}
```
#### Output
```json
{
    "someDate": { "$date": "2016-06-28T15:28:54.721Z" } ,
    "year": 2016
}
```