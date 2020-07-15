[![GitHub](https://img.shields.io/github/license/zycrophat/scala-file-event-processor)](https://raw.githubusercontent.com/zycrophat/scala-file-event-processor/master/LICENSE)
[![Build Status](https://dev.azure.com/zycrophat/scala-file-event-processor/_apis/build/status/zycrophat.scala-file-event-processor?branchName=master)](https://dev.azure.com/zycrophat/scala-file-event-processor/_build/latest?definitionId=3&branchName=master)
[![Release Status](https://vsrm.dev.azure.com/zycrophat/_apis/public/Release/badge/0c5fa297-1f16-4207-aa5e-0b129a8f314d/1/1)](https://dev.azure.com/zycrophat/scala-file-event-processor/_release?view=all&_a=releases&definitionId=1)

# scala-file-event-processor

todo

## Prerequisites

- Tested on JDK 11 (but should work on JDK 8)
- .NET Core SDK 3.1 
 
## How to run tests

``` bash
$ sbt test
```

To run with coverage report:

``` bash
$ sbt test coverageReport
```

A scoverage report will be created in the directory `target/scala-2.13/scoverage-report`.

## How to build

``` bash
$ sbt generateFunctionAppFiles
```

## License

MIT License
