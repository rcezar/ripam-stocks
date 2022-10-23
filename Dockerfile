FROM openjdk:11-jre-slim

ENV SERVER_PORT=8080

COPY target/stock-management-*.jar /usr/local/lib/stock-management.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/stock-management.jar"]