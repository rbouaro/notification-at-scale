FROM eclipse-temurin:26-jdk@sha256:5f85d786b58064f9d653973c7e3df6c36cca7b060f76c55e3f3cda3c3a50d10b AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q
COPY src src
RUN ./mvnw package -DskipTests -q

FROM eclipse-temurin:26-jre@sha256:6272dd10034adf1177526d8c7c095f7190a170adc66b550006b1b40b17119963
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
