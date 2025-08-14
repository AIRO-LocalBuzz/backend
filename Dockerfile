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

# 1) 의존성 캐시 레이어 (kts/groovy/props 모두 커버)
COPY settings.gradle* build.gradle* gradle.properties* ./
RUN gradle --no-daemon --version

# 2) 소스만 복사
COPY src ./src

RUN find /app/src -name "ServerStartupNotifier.java" -print || true
RUN grep -R --line-number "sendMessageToChannelCollectClutrFatvlDataSuccess(" /app/src || true
RUN grep -R --line-number "sendMessageToChannelCollectClutrFatvlDataFail(" /app/src || true

# 3) 클린 빌드 (Spring Boot면 bootJar 권장)
RUN gradle clean bootJar -x test --no-daemon

# ---- runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar AIRO-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "AIRO-0.0.1-SNAPSHOT.jar"]
