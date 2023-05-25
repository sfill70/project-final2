
FROM maven:3.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
COPY resources ./resources
COPY config ./config
COPY doc ./doc
RUN mvn clean package -DskipTests
RUN mv ./target/*.jar ./jira.jar
ENTRYPOINT ["java","-jar","/app/jira.jar", "--spring.profiles.active=prod"]
