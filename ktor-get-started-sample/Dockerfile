# ---------- Etapa 1: build ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon -q > /dev/null

COPY src ./src
RUN ./gradlew buildFatJar --no-daemon

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd --system --no-create-home appuser
COPY --from=build /app/build/libs/*-all.jar app.jar
USER appuser

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]