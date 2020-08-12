Returns a GeoJSON formatted [Point](https://geojson.org/geojson-spec.html#point).

### Options

- `longBounds` (optional) Array of longitude bounds. Default `[-180, 180]`.
- `latBounds` (optional) Array of latitude bounds. Default `[-90, 90]`.

### Example

#### Template
```json
{ "point": { "$polygon": { "longBounds": [-20, -19] } } }
```
#### Output
```json
{ "point": { "type": "Point", "coordinates": [-19.96851, -47.46141] } }
```