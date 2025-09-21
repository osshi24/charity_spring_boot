# Sử dụng Maven để build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# copy file pom.xml và src để build
COPY pom.xml .
COPY src ./src

# chạy package (tạo file jar)
RUN mvn clean package -DskipTests

# Stage 2: chạy app
FROM eclipse-temurin:17-jdk
WORKDIR /app

# copy file jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# bật cổng 8080
EXPOSE 5555

# chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
