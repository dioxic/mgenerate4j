Chooses one element from an array of possible choices with uniform probability.
Optionally chooses with probability proportional to a provided `weights` array.

### Options

- `from` (required) Array of values or operators to choose from.
- `weights` (optional) Array of weightings (must be the same length as `from`).

### Example

#### Template
```json
{ "status": { "$choose": {"from": ["read", "unread", "deleted"], "weights": [2, 1, 1] } } }
```
#### Output
Returns `{"status": "read"}` with probability 1/2,\
`{"status": "unread"}` with probability 1/4,\
`{"status": "deleted"}` with probability 1/4.