Computes the minimum value from an array of elements. All elements must be comparable and of the same type.

### Example
#### Template
```json
{ "minNumber": { "$min": [1,2,3,4,5] } }
{ "minString": { "$min": ["aaa", "abb", "acc"] } }
```
#### Output
```json
{ "minNumber": 1 }
{ "minString": "aaa" }
```