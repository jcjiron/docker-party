# ---------- Etapa 1: build ----------
FROM dart:stable AS build
WORKDIR /app

COPY pubspec.* ./
RUN dart pub get

COPY . .
RUN dart pub get --offline
RUN dart compile exe bin/server.dart -o bin/server

# ---------- Etapa 2: runtime ----------
FROM scratch
COPY --from=build /runtime/ /
COPY --from=build /app/bin/server /app/bin/

ENV TARGET="World"
EXPOSE 8080
CMD ["/app/bin/server"]