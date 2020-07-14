[![GitHub](https://img.shields.io/github/license/zycrophat/datesbetween)](https://raw.githubusercontent.com/zycrophat/datesbetween/master/LICENSE)
# datesbetween

A Scala library for calculating all dates between two given dates.

## Examples

To use the library, import the following:

``` scala
import zycrophat.datesbetween.DatesBetween._
```

A Seq of dates can be calculated as followed:

``` scala
val start = LocalDate.of(2020, 3, 1)
val end = LocalDate.of(2020, 3, 6)

val theDatesBetween = datesBetween(start, end) // will include 2020-03-01, ... , 2020-03-06
```

Using `inclusive` (wihch is the default) and `exclusive` the upper and lower bounds can be adjusted:

``` scala
val start = LocalDate.of(2020, 3, 1)
val end = LocalDate.of(2020, 3, 6)

val theDatesBetween = datesBetween(start, end.exclusive) // will include 2020-03-01, ... , 2020-03-05
```

You can also use LocalDateTime instead of LocalDate:
``` scala
val start = LocalDate.of(2020, 3, 1).atStartOfDay()
val end = LocalDate.of(2020, 3, 6).atTime(13, 37)

val theDatesBetween = datesBetween(start.exclusive, end.exclusive) // will include 2020-03-02, ... , 2020-03-05
```

Or your can use a mixture of different types:
``` scala
val start = LocalDate.of(2020, 3, 1)
val end = LocalDate.of(2020, 3, 6).atTime(13, 37)

val theDatesBetween = datesBetween(start.exclusive, end.exclusive) // will include 2020-03-02, ... , 2020-03-05
```

For other types you must provide an implicit conversion function, e.g.:
``` scala
val start = ZonedDateTime.parse("2020-03-01T10:15:30+01:00")
val end = ZonedDateTime.parse("2020-03-06T13:37:42+02:00")

implicit val zonedDateTimeToLocalDate: ZonedDateTime => LocalDate = (zdt: ZonedDateTime) => zdt.toLocalDate

val theDatesBetween = datesBetween(start.exclusive, end) // will include 2020-03-02, ... , 2020-03-06
```

You can also the `and` method for an even more fluent experience:
``` scala
val start = LocalDate.of(2020, 3, 1)
val end = LocalDate.of(2020, 3, 6).atTime(13, 37)

val theDatesBetween = datesBetween(start.exclusive and end.exclusive, Period.ofDays(2))
```

## Prerequisites

- Tested on JDK 11 (but should work on JDK 8)
 
## How to run tests

``` bash
$ sbt test
```

To run with coverage report:

``` bash
$ sbt test coverageReport
```

A scoverage report will be created in the directory `target/scala-2.11/scoverage-report`.

## How to build

``` bash
$ sbt package
```

## License

MIT License
