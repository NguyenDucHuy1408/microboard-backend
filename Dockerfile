# Giai đoạn 1: Build file .jar bằng Maven (Dùng bản temurin 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy ứng dụng bằng JRE siêu nhẹ của temurin
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","app.jar"]