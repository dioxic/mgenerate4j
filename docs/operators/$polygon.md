Returns a GeoJSON formatted [Polygon](https://geojson.org/geojson-spec.html#polygon).

## Options

- `longBounds` (optional) Array of longitude bounds. Default `[-180, 180]`.
- `latBounds` (optional) Array of latitude bounds. Default `[-90, 90]`.
- `corners` (optional) Number of corners in a polygon. Default `3`. The last point in the coordinates array closes the polygon and does not count towards the number of corners.

## Example

=== "Template"
    ```json
    {
        "line": {
            "$polygon": { "corners": 3 }
        }
    }
    ```
=== "Output"
    ```json
    {
        "line": {
            "type": "Polygon",
            "coordinates": [[
               [
                 157.97981016109566,
                 -4.0566284883708335
               ],
               [
                 -98.81723187898382,
                 -35.139170366023876
               ],
               [
                 -163.00406887762279,
                 88.4324536970949
               ],
               [
                 157.97981016109566,
                 -4.0566284883708335
               ]
            ]]
        }
    }
    ```