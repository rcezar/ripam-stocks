# Stock Management API

## Summary

This API is used to manage stock records based on a collection of records from the [Dow Jones Index from 2011](http://archive.ics.uci.edu/ml/datasets/Dow+Jones+Index#).

AS part of its feature, this allows multiple users to perform the following operations concurrently:
- upload a bulk data set (CSV file in the format above)
- query for data by stock ticker (e.g. input: AA)
- add a new record
- query data by id

### Create stocks in bulk
Here are some special remarks for the feature create stocks in bulk.
- This feature receives file from rest call and stores it on mongoDB (this has been specially catered for even big files, such as greater than 16MB);
- Then it reads the stored file and asynchronously process that file. The csv/format/template is pre-determined.
- In case the application stops abruptly or for some reason it is not able to finish processing the file, the Scheduler.java runs a cron job that reads next 10 files and starts processing each in parallel.
- This cron job can be refactored to run from a cloud based cron job, and the methood could be exposed by an api;

## Building API

### Pre-requisites

* Docker
* Maven

### Building and run service locally

Execute building command from project root directory
```
mvn clean package
```

then build container
```
docker-compose build
```

then run container
```
docker-compose up
```

### Open API

Once application is up and running the open api url can be viewed using bellow url

``` 
http://localhost:8080/swagger-ui/index.html
```