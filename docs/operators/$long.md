Returns a random BSON Long (64-bit) number.

**Aliases:** `$long`, `$mgNumberLong`, `$int64`

### Options

- `min` (optional) minimum value. Default `-2^53`.
- `max` (optional) maximum value. Default `2^53`.

### Example

#### Template
```json
{ "price": { "$long": { "min": 10000 } } }
```
#### Output
```json
{ "price": { "$numberLong": "7624790980443125" } }
```