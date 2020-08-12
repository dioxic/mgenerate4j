Returns a [RegExp](https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/RegExp) object.

### Options

- `string` (optional) The regular expression string. Default `'.*'`.
- `flags` (optional) Flags for the RegExp object. Default `''`.

### Example

#### Template
```json
{ "expr": { "$regex": {"string": "^ab+c$", "flags": "i" } } }
```
#### Output
```json
{ "expr": { "$regularExpression": "^ab+c$", "$options": "i" } }
```