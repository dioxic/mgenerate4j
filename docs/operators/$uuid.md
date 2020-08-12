Returns a random UUID.

### Options

- `type` (optional) The data type of the UUID (`BINARY` or `STRING`). Default `BINARY`.

### Example

#### Template
```json
{ "uuid": "$uuid" }
{ "stringUuid": { "$uuid": "STRING" } }
```
#### Output
```json
{ "uuid": { "$binary": { "base64": "LkYPJF2By38k2KfYBdwIhQ==", "subType": "04" } } }
{ "stringUuid": "606340f0-fceb-419b-91c5-fdf192d08fad" }
```