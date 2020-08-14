# Operator Reference

This is a full list of all the out-of-the-box operators. If you want to create your own operator, check out the [Custom Operators][custom-operators] page.

## General

- [`$avg`][$avg]: Calculates the average of the input array elements.
- [`$array`][$array]: Creates an array of values.
- [`$bin`][$bin]: Creates random binary data of a specified size.
- [`$boolean`][$boolean]: Creates a random boolean value.
- [`$concat`][$concat]: Merges multiple arrays into a single array.
- [`$choose`][$choose]: Chooses one element from an array of possible choices.
- [`$distinct`][$distinct]: Returns the distinct set of the input array.
- [`$hash`][$hash]: Returns the hash of the input.
- [`$inc`][$inc]: Increments the input value.
- [`$join`][$join]: Joins elements of an array to a string.
- [`$max`][$max]: Returns the max value from an array of elements.
- [`$min`][$min]: Returns the min value from an array of elements.
- [`$mod`][$mod]: Returns the modulus of the input.
- [`$objectId`][$objectId]: Returns a new ObjectId.
- [`$optional`][$optional]: A wrapper to optionally include/exclude the value.
- [`$pick`][$pick]: Returns an element from an array.
- [`$pickset`][$pickSet]: Returns a subset of an array.
- [`$regex`][$regex]: Returns a Regular Expression object.
- [`$uuid`][$uuid]: Generates a random UUID.

## Sequence

- [`$dateSequence`][$dateSequence]: Returns a sequence of dates.
- [`$intSequence`][$intSequence]: Returns a sequence of 32-bit integers.
- [`$longSequence`][$longSequence]: Returns a sequence of 64-bit integers.

## Numeric

- [`$floating`][$floating]: Returns a random floating point.
- [`$decimal`][$decimal]: Returns a random Decimal 128.
- [`$int`][$int]: Returns a random 32-bit integer.
- [`$long`][$long]: Returns a random 64-bit integer.

## Time / Date

- [`$dt`][$dt]: Returns a random date, optionally in a given range.
- [`$now`][$now]: Returns the current date.
- [`$dayOfMonth`][$dayOfMonth]: Day of month of input date.
- [`$dayOfWeek`][$dayOfWeek]: Day of week of input date.
- [`$dayOfYear`][$dayOfYear]: Day of year of input date.
- [`$epoch`][$epoch]: Epoch of input date.
- [`$hour`][$hour]: Hour of input date.
- [`$minute`][$minute]: Minute of input date.
- [`$month`][$month]: Month of input date.
- [`$second`][$second]: Second of input date.
- [`$ts`][$ts]: Creates a random timestamp.
- [`$year`][$year]: Year of input date.

## Text

- [`$character`][$character]: Returns a random character from a pool of characters.
- [`$paragraph`][$paragraph]: Returns a random paragraph.
- [`$sentence`][$sentence]: Returns a random sentence.
- [`$string`][$string]: Returns a string of a defined length.
- [`$word`][$word]: Returns a random word.

## Geospatial

- [`$coordinates`][$coordinates]: Returns a pair of longitude/latitude coordinates.
- [`$point`][$point]: Returns a GeoJSON Point.
- [`$lineString`][$lineString]: Returns a GeoJSON LineString.
- [`$polygon`][$polygon]: Returns a GeoJSON Polygon.

## Internet

- [`$domain`][$domain]: Returns a random www domain name.
- [`$email`][$email]: Returns a random email address.
- [`$ip`][$ip]: Generates a random IPv4 address.
- [`$ipv6`][$ipv6]: Generates a random IPv6 address.
- [`$macAdress`][$macAdress]: Generates a random MAC address.
- [`$url`][$url]: Generates a random URL.
- [`$username`][$username]: Generates a random username.

## Location

- [`$city`][$city]: Generates a random city.
- [`$country`][$country]: Generates a random country.
- [`$phone`][$phone]: Generates a random phone number.
- [`$postal`][$postal]: Generates a random post code.
- [`$state`][$state]: Generates a random state.
- [`$street`][$street]: Generates a random street.
- [`$zip`][$zip]: Generates a random zip code.

## Person

- [`$age`][$age]: Generates a random age.
- [`$birthday`][$birthday]: Generates a random birthday.
- [`$first`][$first]: Generates a random first name.
- [`$gender`][$gender]: Generates a random gender.
- [`$last`][$last]: Generates a random last name.
- [`$maritalStatus`][$maritalStatus]: Generates a random marital status.
- [`$name`][$name]: Generates a name.
- [`$prefix`][$prefix]: Generates a random salutation.
- [`$ssn`][$ssn]: Generates a random social security number.
- [`$suffix`][$suffix]: Generates a random title (e.g. esq.).


[custom-operators]:  custom-operators.md

[$array]:            operators/$array.md
[$avg]:              operators/$avg.md
[$bin]:              operators/$bin.md
[$boolean]:          operators/$boolean.md
[$character]:        operators/$character.md
[$choose]:           operators/$choose.md
[$concat]:           operators/$concat.md
[$coordinates]:      operators/$coordinates.md
[$dateSequence]:     operators/$dateSequence.md
[$dayOfMonth]:       operators/$dayOfMonth.md
[$dayOfWeek]:        operators/$dayOfWeek.md
[$dayOfYear]:        operators/$dayOfYear.md
[$decimal]:          operators/$decimal.md
[$distinct]:         operators/$distinct.md
[$dt]:               operators/$dt.md
[$epoch]:            operators/$epoch.md
[$floating]:         operators/$floating.md
[$hash]:             operators/$hash.md
[$hour]:             operators/$hour.md
[$inc]:              operators/$inc.md
[$integer]:          operators/$integer.md
[$intSequence]:      operators/$intSequence.md
[$join]:             operators/$join.md
[$lineString]:       operators/$lineString.md
[$long]:             operators/$long.md
[$longSequence]:     operators/$longSequence.md
[$max]:              operators/$max.md
[$min]:              operators/$min.md
[$minute]:           operators/$minute.md
[$mod]:              operators/$mod.md
[$month]:            operators/$month.md
[$now]:              operators/$now.md
[$objectId]:         operators/$objectId.md
[$optional]:         operators/$optional.md
[$paragraph]:        operators/$paragraph.md
[$pick]:             operators/$pick.md
[$pickSet]:          operators/$pickSet.md
[$point]:            operators/$point.md
[$polygon]:          operators/$polygon.md
[$regex]:            operators/$regex.md
[$second]:           operators/$second.md
[$sentence]:         operators/$sentence.md
[$string]:           operators/$string.md
[$ts]:               operators/$ts.md
[$uuid]:             operators/$uuid.md
[$word]:             operators/$word.md
[$year]:             operators/$year.md