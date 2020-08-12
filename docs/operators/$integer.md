Returns a random BSON Integer (32-bit) number.

**Aliases:** `$integer`, `$number`, `$int`, `$mgNumberInt`, `$int32`

### Options

- `min` (optional) minimum value. Default `-2^31`.
- `max` (optional) maximum value. Default `2^31`.

### Example

#### Template
```json
{ "price": { "$int": { "min": 100 } } }
```
#### Output
```json
{ "price": 7647 } }
```