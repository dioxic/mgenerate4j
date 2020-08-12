Returns a GeoJSON formatted [LineString](https://geojson.org/geojson-spec.html#linestring).

### Options

- `longBounds` (optional) Array of longitude bounds. Default `[-180, 180]`.
- `latBounds` (optional) Array of latitude bounds. Default `[-90, 90]`.
- `locs` (optional) Number of locations in the line string. Default `3`.

### Example

#### Template
```json
{ "line": { "$lineString": { "locs": 3 } } }
```
#### Output
```json
{ "line": { "type": "LineString", "coordinates": [[102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]] } }
```