FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java", "-jar", "skincare-products-sales-system-api.jar"]

EXPOSE 8080