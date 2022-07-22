## Tests with using [jsonplaceholder](https://jsonplaceholder.typicode.com/)

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/KateMyr/test-api-task/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/KateMyr/test-api-task/tree/main)

### About
This project is a part of the test task given by one of the companies.

### Preconditions for the local run
1. [Maven](https://maven.apache.org/install.html)
2. [Java 11](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-macos.html)
3. [Allure](https://docs.qameta.io/allure/)

### How to run
Run tests
```bash
$ mvn clean test
```

Then, to build Allure report run

```bash
$ mvn allure:report
```

In order to view the report run

```bash
$ mvn allure:serve
```
