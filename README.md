# ttrpg-api

Just a little toy HTTP API built using Ktor.

## Application built with

* [Kotlin](https://kotlinlang.org/)
* [Ktor](https://ktor.io/)
* [Exposed](https://github.com/JetBrains/Exposed)
* [HikariCP](https://github.com/brettwooldridge/HikariCP)
* [PostgreSQL](https://www.postgresql.org/)
* [Gradle](https://gradle.org/)
* [Docker](https://www.docker.com/)

## Running

1. Create the JAR file.
```bash
$ ./gradlew shadowJar
```
2. Create the docker image.
```bash
$ docker-compose build
```
3. Start the containers.
```
$ docker-compose up
```