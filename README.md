# kotlin-spring-boot-jooq-pebble-example

## Features

* kotlin v1.3
* Gradle v5.4
* Java v11
* Spring boot v2.1
* jOOQ (ORM)
* Liquibase
* thymeleaf v3
* MySQL v8
* Redis v5
* Docker
* MailCatcher (Virtual smtp and local webmailer)
* HikariCP (Fast and eco connection pooling)
* Lettuce redis client
* Yarn
* apiDoc (API definition)

### Api project features

* Webflux
* Router function
* jOOQ ORM
* Repository cache for redis

### Admin project features

* Webflux
* Annotation base controller
* Reactive spring security
* Role management for MySQL
* Thymeleaf view
* Session cache for redis

### Batch project features

* Not use spring batch (very difficult...)
* Multiple CommandLineRunner (easy and simple)

## Requirement

* macOS
* IntelliJ IDEA

## Project

### Project structure

```
database
└── base
      ├── api
      ├── admin
      └── batch
```

### Project role

| project name | Executable jar | description |
|:----|:---|:---|
|database|☓|generate-jooq, migrate-liquibase, auto-generated jooq files|
|base|☓|share project|
|api|◯|api project|
|admin|◯|admin project|
|batch|◯|batch project|

## Setup

### Install sdkman

Install java by [sdkman](https://sdkman.io/install).

```bash
# check java versions
$ sdk list java | grep zulu
     11.0.2-zulu
     11.0.1-zulu
     11.0.1-zulufx
     10.0.2-zulu
     9.0.7-zulu
     8.0.201-zulu
     8.0.192-zulufx
     7.0.181-zulu
# install java11
sdk install java 11.0.2-zulu
```

### Install docker for mac

https://docs.docker.com/docker-for-mac/install/

### Git clone project

```bash
# clone project
git clone https://github.com/treetips/kotlin-spring-boot-jooq-liquibase-thymeleaf-example.git
cd kotlin-spring-boot-jooq-liquibase-thymeleaf-example
```

### Install node_modules

```bash
cd ${PROJECT_ROOT}
yarn
```

### Install IntelliJ IDEA

Install community(or ultimate) edition.

https://www.jetbrains.com/idea/download/#section=mac

### Install IntelliJ IDEA plugin

* Start IntelliJ IDEA.
* `Welcome to IntelliJ IDEA` → `Configure` → `Plugins` → `Install JetBrains plugin` . Install `Editorconfig` .

### Import sample project

* Start IntelliJ IDEA.
* `Welcome to IntelliJ IDEA` → `Configure` → `Project Defaults` → `Project Structure` .
* `SDKs` → `+` → `JDK` -> `$HOME/.sdkman/candidates/java/11.0.2-zulu` .
* `Welcome to IntelliJ IDEA` -> `Import Project` -> choose clone directory.
* `Import from external project model` → `Gradle` -> Next.
* Check to `Use auto-import` and `Use gradle 'wrapper' task configuration`. Choose `Gradle JVM` to `Use JAVA_HOME(sdkman)` . Choose `Project format` to `.idea (directory based)` .
* Startup IntelliJ IDEA, jar file downloading of gradle is automatically started. Wait for few minutes.

## Usage

### Start docker daemon

Require starting docker daemon.

### Start docker-compose

```bash
cd ${PROJECT_ROOT}
docker-compose up -d
```

Start to `MySQL-server` and `Redis-server` and `MailCatcher(Virtual SMTP)`.

### Initialize database

```bash
cd $PROJECT_ROOT/database
./migrate-up.sh
```

Initial table and initial data are built by Liquibase.

### Generate jOOQ

```bash
cd $PROJECT_ROOT/database
./generate-jooq.sh
```

jOOQ generate java files by initialized database.

### Start API

Right click on `com.example.api.ApiApplication.kt` -> Run.

Browse `http://localhost:8080/api/prefecture/`

### Start Admin

Right click on `com.example.admin.AdminApplication.kt` -> Run.

Browse `http://localhost:8090/`

### Start Batch

* Menubar -> Run -> Edit configurations -> Open Run/Debug Configurations window 
* Select Spring boot -> click + -> click Spring boot -> Input below and OK and Run Batch !

| Setting    | Value                              |
| ---------- | ---------------------------------- |
| Main class | com.example.batch.BatchApplication |
| VM options | -Dbatch.name=Sample1Batch          |

## Optional

### Open webmailer(MailCatcher)

`docker-compose up -d`, browse `http://localhost:1080` .

### Generate api definition by apiDoc

```bash
cd $PROJECT_ROOT
yarn apidoc
```

Apidoc is displayed automatically by a browser when execute this command.

### Generate database definition by Liquibase

```bash
cd $PROJECT_ROOT/database
./generate-dbdoc.sh
```

Browse `database/build/database/docs/index.html` .

### Connect to local MySQL-server on docker

```bash
cd $PROJECT_ROOT
./connect-mysql.sh
```

### Connect to local Redis-server on docker

```bash
cd $PROJECT_ROOT
./connect-redis.sh
```
