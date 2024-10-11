FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -f pom.xml clean package -DskipTests

FROM openjdk:21
WORKDIR  /
COPY /src /src
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]