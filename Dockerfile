#FROM openjdk:21
#
#ARG JAR_FILE=target/*.jar
#
#ADD ${JAR_FILE} api-service.jar
#
#ENTRYPOINT ["java","-jar","api-service.jar"]
#
#EXPOSE 8080

# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.8-eclipse-temurin-21 AS build


# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 21
FROM amazoncorretto:21.0.4

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]