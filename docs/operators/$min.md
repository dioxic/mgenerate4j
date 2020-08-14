Computes the minimum value from an array of elements. All elements must be comparable and of the same type.

## Example

=== "Template"
    ```json
    { "minNumber": { "$min": [6,2,3,4,5] } }
    { "minString": { "$min": ["zzz", "aaa", "ggg"] } }
    ```
=== "Output"
    ```json
    { "minNumber": 2 }
    { "minString": "aaa" }
    ```