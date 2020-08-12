Returns a sequence of 32-bit integers.

**Aliases:** `$seq`, `$intSeq`, `$sequence`, `$intSequence`

### Options

- `start` (optional) Initial value. Default `0`.
- `step` (optional) The step increment. Default `1`.

### Example

#### Template
```json
{ "seq": { "$intSeq" : { "step": 2 } } }
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