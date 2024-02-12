# Personal Website API (PW-API)
## Contents
 - [Overview](#overview) 
 - [Setup](#setup)
     - [Prerequisites](#prerequisites)
     - [Environment Variables](#environment-variables)
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
- Java 17 or later
- Apache Maven

### Environment Variables
The following environment variables need to be set in order for PW-API to work:
|NAME|DESCRIPTION|
|----|-----------|
|PW-API_JWT_SECRET|This is the secret needed to sign the JSON Web Token (JWT) for PW-API. Specifically, this JWT is the bearer token used by the client. To generate a secure secret, reference [this section](#generating-a-secret-for-signing-the-bearer-token).|
|PW-API_DATASOURCE_USERNAME|As of now, this is how the local H2 database used by PW-API gets its console username. This will be changed in the future to be externalized. This should only be used for testing purposes.|
|PW-API_DATASOURCE_PASSWORD|As of now, this is how the local H2 database used by PW-API gets its console password. This will be changed in the future to be externalized. This should only be used for testing purposes.|

## Building the application
To build the application, you must have Apache Maven installed. Run the following command:
```sh
mvn clean package
```

## Running the application locally
To run the application using the Apache Maven Spring Boot plugin, you must have Apache Maven installed. Run the following command to run the application locally:
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