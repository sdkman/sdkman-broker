# SDKMAN Broker Service

![Build status](https://github.com/sdkman/sdkman-broker/actions/workflows/release.yml/badge.svg)
![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/sdkman/sdkman-broker)

This service is responsible for determining the location of remote binaries (third party and owned) through a unified API.

## Run local

You will need to have MongoDB up and running locally on the default port.

    $ docker run -d --net=host mongo:latest

We can now run the app up locally with a simple

    $ ./gradlew run

## Tests

The service has a comprehensive suite of standalone acceptance tests and unit tests.

		$ ./gradlew check
