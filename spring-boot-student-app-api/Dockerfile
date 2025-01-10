# Bước 1: Sử dụng một image có Maven để build ứng dụng
FROM maven:3.8.6-openjdk-8-slim AS build

# Thiết lập thư mục làm việc cho Maven
WORKDIR /app

# Copy pom.xml và các file cần thiết để build ứng dụng
COPY pom.xml .

# Download tất cả dependencies của ứng dụng
RUN mvn dependency:go-offline

# Copy toàn bộ mã nguồn và build ứng dụng
COPY src /app/src

# Build ứng dụng với Maven
RUN mvn clean package -DskipTests

# Bước 2: Tạo image cho ứng dụng với OpenJDK
FROM openjdk:8-jdk-alpine

# Thiết lập thư mục để chứa ứng dụng trong container
WORKDIR /usr/share/myservice

# Copy file JAR đã build từ build image
COPY --from=build /app/target/student-app-api-0.0.1-SNAPSHOT.jar /usr/share/myservice/student-app-api-0.0.1-SNAPSHOT.jar

# Thiết lập entrypoint để chạy ứng dụng
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/myservice/student-app-api-0.0.1-SNAPSHOT.jar"]

# Mở cổng 8080 cho ứng dụng
EXPOSE 8080
