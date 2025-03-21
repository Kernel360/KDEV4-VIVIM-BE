# 🔹 1단계: 빌드 환경
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Gradle wrapper 및 의존성 파일 캐싱
COPY settings.gradle build.gradle gradle.properties gradlew ./
COPY gradle gradle
# 멀티모듈 환경에서는 전체 프로젝트 복사 필요
COPY . .

# 의존성 캐싱을 위한 빠른 빌드
RUN ./gradlew clean :module-api:bootJar -x test

# 🔹 2단계: 실행 환경 (최소 JDK 환경)
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드된 bootJar 복사 (module-api만)
COPY --from=builder /app/module-api/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
