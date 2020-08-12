Returns a GeoJSON formatted [Polygon](https://geojson.org/geojson-spec.html#polygon).

### Options

- `longBounds` (optional) Array of longitude bounds. Default `[-180, 180]`.
- `latBounds` (optional) Array of latitude bounds. Default `[-90, 90]`.
- `corners` (optional) Number of corners in a polygon. Default `3`. The last point in the coordinates array closes the polygon and does not count towards the number of corners.

### Example

#### Template
```json
{ "line": { "$polygon": { "corners": 3 } } }
```
#### Output
```json
{ "line": { "type": "Polygon", "coordinates": [[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]] } }
```