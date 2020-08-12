Returns a sequence of 64-bit integers.

**Aliases:** `$longSeq`, `$longSequence`

### Options

- `start` (optional) Initial value. Default `0`.
- `step` (optional) The step increment. Default `1`.

### Example

#### Template
```json
{ "seq": { "$longSeq" : { "step": 2 } } }
```
#### Output
```json
{ "seq": 0 }
{ "seq": 2 }
{ "seq": 4 }
{ "seq": 6 }
{ "seq": 8 }
...
```