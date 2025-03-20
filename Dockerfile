# 🔹 1단계: 빌드 환경 (Gradle 빌드)
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Gradle 캐싱을 위해 의존성 관련 파일 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle build || return 0  # Gradle 캐시 적용

# 전체 소스 복사 후 빌드 실행
COPY . .
RUN gradle clean build -x test

# 🔹 2단계: 실행 환경 (JVM만 포함)
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드된 JAR 파일을 실행 환경으로 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너 실행 시 Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]