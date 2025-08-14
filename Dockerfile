#FROM gradle:8.7.0-jdk21 AS build
#WORKDIR /app
#
#COPY --chown=gradle:gradle build.gradle settings.gradle ./
#
#RUN gradle dependencies
#
#COPY --chown=gradle:gradle . .
#
#RUN gradle build -x test
#
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#
#COPY --from=build /app/build/libs/*.jar AIRO-0.0.1-SNAPSHOT.jar
#
#ENTRYPOINT ["java", "-jar", "AIRO-0.0.1-SNAPSHOT.jar"]


# ---- builder ----
FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /app

# 1) 의존성 레이어
COPY --chown=gradle:gradle settings.gradle* build.gradle* gradle gradlew ./
RUN chmod +x gradlew
RUN ./gradlew --version

# 2) 소스만 복사
COPY --chown=gradle:gradle src ./src

# 3) 항상 깨끗하게 bootJar 생성
RUN ./gradlew clean bootJar -x test --no-daemon

# ---- runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar AIRO-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "AIRO-0.0.1-SNAPSHOT.jar"]
