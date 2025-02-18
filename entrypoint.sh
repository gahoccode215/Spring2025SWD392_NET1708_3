#!/bin/bash

# Load các biến môi trường từ file .env vào môi trường shell
if [ -f /app/.env ]; then
  set -a
  source /app/.env
  set +a
fi

echo "Loaded SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}"
echo "Loaded AZURE_DB_HOST=${AZURE_DB_HOST}"

# Chạy ứng dụng Spring Boot với profile từ biến môi trường
exec java -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}
