# Use a multi-stage build to produce a small final image
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml mvn .
COPY src ./src
RUN mvn -B -DskipTests package


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/target/calculator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
