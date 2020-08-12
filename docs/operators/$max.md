Computes the maximum value from an array of elements. All elements must be comparable and of the same type.

### Example
#### Template
```json
{ "maxNumber": { "$max": [1,2,3,4,5] } }
{ "maxString": { "$max": ["aaa", "abb", "acc"] } }
```
#### Output
```json
{ "maxNumber": 5 }
{ "maxString": "acc" }
```