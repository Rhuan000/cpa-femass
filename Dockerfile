# Stage 1: build the application
FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /workspace
# copy maven wrapper and pom for caching
COPY mvnw pom.xml .mvn/ ./
COPY .mvn .mvn
# copy source
COPY src src

# package the application (skip tests to speed up in CI; change if you want tests)
RUN mvn -B clean package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:21-jre
WORKDIR /work
COPY --from=build /workspace/target/quarkus-app/ /work/quarkus-app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/work/quarkus-app/quarkus-run.jar"]
