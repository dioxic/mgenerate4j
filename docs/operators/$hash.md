Computes a hash of the input value.

### Options

- `input` (required) The value to hash.
- `algorithm` (optional) The hash algorithm to use: `MD5`, `SHA1`, `SHA256`, `HASHCODE` (default: `MD5`).

### Example

#### Template
```json
{ "hash": { "$hash": "abc" } }
```
#### Output
```json
{ "hash": 572197704 }
```