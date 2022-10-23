# Stock Management API

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
docker build -t stock-management-docker .
```

then run container
```
docker run -p 8080:8080 stock-management-docker
```

### Open API

Once application is up and running the open api url can be viewed using bellow url

``` 
http://localhost:8080/swagger-ui/index.html
```