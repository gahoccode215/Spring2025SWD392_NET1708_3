# Stage 1: Build ứng dụng Spring Boot
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy file cấu hình Maven và source code vào container
COPY pom.xml .
COPY src ./src

# Build ứng dụng Spring Boot bằng Maven
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy ứng dụng
FROM amazoncorretto:21.0.4

# Đặt thư mục làm việc cho container
WORKDIR /app

# Sao chép file .env vào container (nếu có)
COPY .env.prod /app/.env

# Sao chép file JAR đã build từ Stage 1 vào container
COPY --from=build /app/target/*.jar app.jar

# Sao chép script entrypoint.sh để load các biến môi trường
#COPY entrypoint.sh /app/entrypoint.sh
#RUN chmod +x /app/entrypoint.sh

# Expose port 8080 để Spring Boot có thể truy cập
#EXPOSE 8080

# Chạy ứng dụng bằng script entrypoint.sh
#ENTRYPOINT ["/app/entrypoint.sh"]
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]