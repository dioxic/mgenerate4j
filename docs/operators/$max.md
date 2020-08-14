Computes the maximum value from an array of elements. All elements must be comparable and of the same type.

## Example
=== "Template"
    ```json
    { "maxNumber": { "$max": [1,2,5,4,3] } }
    { "maxString": { "$max": ["aaa", "zzz", "ggg"] } }
    ```
=== "Output"
    ```json
    { "maxNumber": 5 }
    { "maxString": "zzz" }
    ```