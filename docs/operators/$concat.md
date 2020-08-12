Combines the elements of the input array into a single array.

### Options

- `values` (required) Array of values to be joined, the elements can be arrays themselves.

### Example

#### Template
```json
{
    "fish": ["plaice", "haddock", "halibut"],
    "birds": ["duck", "pigeon"],
    "merged": {
        "$concat": [
            "${fish}",
            "${birds}",
            ["badger", "turtle"],
            "$number"
        ]
    }
}
```
#### Output
```json
{
    "fish": ["plaice", "haddock", "halibut"],
    "birds": ["duck", "pigeon"],
    "merged": ["plaice", "haddock", "halibut", "duck", "pigeon", "badger", "turtle", 345633]
}
```