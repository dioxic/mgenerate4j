Returns a BSON [Decimal128](https://github.com/mongodb/specifications/blob/master/source/bson-decimal128/decimal128.rst) number.

**Aliases:** `$decimal`, `$decimal128`, `$mgNumberDecimal`

### Options

- `min` (optional) minimum value. Default `0`.
- `max` (optional) maximum value. Default `1000`.
- `fixed` (optional) number of digits after the decimal. Default `2`.

### Example

#### Template
```json
{ "price": { "$decimal": { "fixed": 3 } } }
```
#### Output
```json
{ "price": { "$numberDecimal": "545.241" } }
```