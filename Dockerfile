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


FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /app

COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts
COPY gradle gradle
COPY gradlew gradlew
RUN ./gradlew --version

RUN rm -rf /app/src
COPY src ./src

# 항상 깨끗하게 빌드
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar AIRO-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "AIRO-0.0.1-SNAPSHOT.jar"]