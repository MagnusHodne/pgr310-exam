FROM maven:3-adoptopenjdk-11 as Builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package

FROM adoptopenjdk/openjdk11
COPY --from=Builder /app/target/*.jar app/application.jar
ENTRYPOINT ["java", "-jar", "/app/application.jar"]