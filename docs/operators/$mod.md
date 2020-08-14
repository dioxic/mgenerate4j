Computes the modulus of the input value.

## Options

- `input` (required) The dividend. Must be numeric.
- `mod` (optional) The divisor. Must be numeric. Default `720`.

## Example

=== "Template"
    ```json
    { "modulus": { "$mod": 721 } }
    { "modulus10": { "$mod": { "input":  14, "mod": 10 } } }
    ```
=== "Output"
    ```json
    { "modulus": 1 }
    { "modulus10": 4 }
    ```