Creates an array of values. Each element is evaluated separately.

### Options

- `of` (required) Defines an element of the array.
- `number` (optional) Number of elements. Default `5`.

### Example
#### Template
```json
{ "countries": { "$array": {"of": "$country"}, "number": 3 } }
```
#### Output
```json
{ "countries": ["Czech Republic", "Ireland", "Argentina"] }
```