FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} skincare-products-sales-system-api.jar

ENTRYPOINT ["java", "-jar", "skincare-products-sales-system-api.jar"]

EXPOSE 8080


