Takes an array `array` and a separator string `sep` and joins the elements
of the array (each cast to string) separated by `sep`.

### Options

- `array` (required) Array of values to be joined.
- `sep` (optional) Separator string. Default `''` (empty string).

### Example

#### Template
```json
{ "code": { "$join": { "array": ["foo", "bar", "baz"], "sep": "-" } } }
```
#### Output
```json
{ "code": "foo-bar-baz" }
```