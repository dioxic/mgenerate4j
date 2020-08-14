Returns a GeoJSON formatted [LineString](https://geojson.org/geojson-spec.html#linestring).

## Options

- `longBounds` (optional) Array of longitude bounds. Default `[-180, 180]`.
- `latBounds` (optional) Array of latitude bounds. Default `[-90, 90]`.
- `locs` (optional) Number of locations in the line string. Default `3`.

## Example

=== "Template"
    ```json
    {
        "line": {
            "$lineString": { "locs": 3 }
        }
    }
    ```
=== "Output"
    ```json
    {
        "line": {
            "type": "LineString",
            "coordinates": [
                [
                  -5.735021154016692,
                  -59.374717617075106
                ],
                [
                  149.858471917435,
                  -67.48631323418616
                ],
                [
                  36.22877102282027,
                  -48.52554693407486
                ]
            ]
        }
    }
    ```