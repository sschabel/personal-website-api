# Personal Website API (PW-API)
## Contents
 - [Overview](#overview) 
 - [Setup](#setup)
     - [Prerequisites](#prerequisites)
     - [Environment Variables](#environment-variables)
     - [Enabling HTTPS for local development](#enabling-https-for-local-development)
 - [Building the application](#building-the-application)
 - [Running the application locally](#running-the-application-locally)
 - [Generating a secret for signing the bearer token](#generating-a-secret-for-signing-the-bearer-token)
## Overview
Backend API for my personal website UI. This is just for learning purposes.

> [!NOTE]
> This is a work in progress. Still need to enable HTTPS and make changes to use a different datasource (something other than H2).

## Setup

### Prerequisites
You need the following software installed:
- Java 21 or later

Optional software:
- Apache Maven (since this project includes a Apache Maven wrapper)

### Environment Variables
The following environment variables need to be set in order for PW-API to work:
|NAME|DESCRIPTION|REQUIRED or OPTIONAL|OPTIONS|DEFAULT|
|----|-----------|--------------------|-------|-------|
|PW-API_JWT_SECRET|This is the secret needed to sign the JSON Web Token (JWT) for PW-API. Specifically, this JWT is the bearer token used by the client. To generate a secure secret, reference [this section](#generating-a-secret-for-signing-the-bearer-token).|Required|
|PW-API_DATASOURCE_USERNAME|As of now, this is how the local H2 database used by PW-API gets its console username. This will be changed in the future to be externalized. This should only be used for testing purposes.|Required|
|PW-API_DATASOURCE_PASSWORD|As of now, this is how the local H2 database used by PW-API gets its console password. This will be changed in the future to be externalized. This should only be used for testing purposes.|Required|
|PW-API_H2_CONSOLE_ENABLED|This controls whether the H2 database console will be able to be accessed locally via the H2 endpoint specified (/api/h2-console)|Optional|``true`` or ``false``|``false``|
|PW-API_KEYSTORE_TYPE|The type of keystore you are using to hold your certificate.|Optional|``PKCS12`` or ``JKS``|``PKCS12``|
|PW-API_KEYSTORE_PATH|The full path to your keystore including the keystore file name.|Required|
|PW-API_KEYSTORE_PASSWORD|The password for your keystore.|Required|
|PW-API_KEYSTORE_ALIAS|The alias for your keystore.|Required|
|PW-API_RECAPTCHA_SECRET|This is the secret token issued by Google's ReCaptcha admin console. See the [ReCaptcha documentation](https://developers.google.com/recaptcha/docs/v3) for more details|Required|

### Enabling HTTPS for local development
Run the following command to generate a self-signed certificate:
```sh
keytool -genkeypair -alias <insert-alias> -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore <insert-keystore-name>.p12 -validity 3650
```
Then, update the below environment variables specified in the [Environment Variables](#environment-variables) section.
  - PW-API_KEYSTORE_PATH
  - PW-API_KEYSTORE_PASSWORD
  - PW-API_KEYSTORE_ALIAS
## Building the application
To build the application, you can use the provided Apache Maven wrapper or use your own installed version of Apache Maven. 

For using the provided Apache Maven wrapper, run the following command:
```sh
mvnw clean package
```

For using your own Apache Maven installation, run the following command:
```sh
mvn clean package
```

## Running the application locally
To run the application using the Apache Maven Spring Boot plugin, you can use the provided Apache Maven wrapper or use your own installed version of Apache Maven.

To use the Apache Maven wrapper, run the following command to run the application locally:
```sh
mvnw spring-boot:run
```
To use your own Apache Maven install, run the following command to run the application locally:
```sh
mvn spring-boot:run
```
You can also run the application via Java using the following command (but you have to [build the app](#building-the-app) first with Apache Maven):
```sh
java -jar personal-website-api-<insert-version-here>.jar

// Example with version filled out:
java -jar personal-website-api-0.0.1-SNAPSHOT.jar
```

## Generating a secret for signing the bearer token
Open SSL command to create a random base 64 string of specified size (in the example below, 32 bytes):
```sh
openssl rand -base64 32
```