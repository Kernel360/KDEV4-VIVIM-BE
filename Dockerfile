# 🔹 1단계: 빌드 환경
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# 멀티모듈 환경에서는 전체 프로젝트 복사 필요
COPY . .

# 의존하는 다른 모듈들까지 빌드
RUN ./gradlew clean :module-api:bootJar --stacktrace --info --refresh-dependencies -x test


# 🔹 2단계: 실행 환경 (최소 JDK 환경)
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드된 bootJar 복사 (module-api만)
COPY --from=builder /app/module-api/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
