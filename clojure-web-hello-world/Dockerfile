# ---------- Etapa 1: build ----------
FROM clojure:temurin-8-lein AS build
WORKDIR /app

COPY project.clj ./
RUN lein deps

COPY src ./src
RUN lein uberjar && \
    find target -name "*-standalone.jar" -exec cp {} /app/app.jar \;

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:8-jre
WORKDIR /app

RUN useradd --system --no-create-home appuser
COPY --from=build /app/app.jar app.jar
USER appuser

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]