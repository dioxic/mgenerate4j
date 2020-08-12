Creates random binary data of a specified size (in bytes)

### Options

- `size` (optional) Number of bytes to generate (Default: `1024`).
- `subtype` (optional) Binary subtype value: `BINARY`, `FUNCTION`, `OLD_BINARY`, `UUID_LEGACY`, `UUID_STANDARD`, `MD5`, `USER_DEFINED`. (Default: `BINARY`).

### Example

#### Template
```json
{ "binary": { "$bin": 10 } }
```
#### Output
```json
{
  "binary": {
    "$binary": {
      "base64": "el2mrgjnZcbMlQ==",
      "subType": "00"
    }
  }
}
```