# docker-party

Four sample applications, each written in a different language and running in
its own container. Three run as single containers, and WordPress runs with
Docker Compose alongside a MySQL database. All four images are published on
Docker Hub for both `linux/amd64` and `linux/arm64`, so Docker pulls the right
one for your machine.

## Applications

| App | Stack | Folder | Docker Hub image | Local URL |
| --- | --- | --- | --- | --- |
| Ktor hello world | Kotlin, JDK 17 | [`ktor-get-started-sample`](ktor-get-started-sample) | [`jeanjiron/kotlin-ktor:1.0`](https://hub.docker.com/r/jeanjiron/kotlin-ktor) | <http://localhost:8081> |
| Shelf hello world | Dart | [`cloudruntest`](cloudruntest) | [`jeanjiron/dart-shelf:1.0`](https://hub.docker.com/r/jeanjiron/dart-shelf) | <http://localhost:8082> |
| Ring hello world | Clojure, JDK 8 | [`clojure-web-hello-world`](clojure-web-hello-world) | [`jeanjiron/clojure-hello:1.0`](https://hub.docker.com/r/jeanjiron/clojure-hello) | <http://localhost:8083> |
| WordPress | PHP 8.3, Apache, MySQL 8.4 | [`WordPress`](WordPress) | [`jeanjiron/wordpress:1.0`](https://hub.docker.com/r/jeanjiron/wordpress) | <http://localhost:8084> |

## Prerequisites

- Docker
- Docker Compose v2 (for WordPress)

## Run from Docker Hub

These commands use the published images, so you don't need to build anything.

```bash
docker run -d --name kotlin-ktor -p 8081:8080 jeanjiron/kotlin-ktor:1.0
docker run -d --name dart-shelf -p 8082:8080 jeanjiron/dart-shelf:1.0
docker run -d --name clojure-hello -p 8083:8080 jeanjiron/clojure-hello:1.0
```

WordPress also needs its database, so start it with Compose as described in
[WordPress with Docker Compose](#wordpress-with-docker-compose).

## Build the images

Run these from the repository root. Each image gets the same name it has on
Docker Hub.

```bash
docker build -t jeanjiron/kotlin-ktor:1.0 ./ktor-get-started-sample
docker build -t jeanjiron/dart-shelf:1.0 ./cloudruntest
docker build -t jeanjiron/clojure-hello:1.0 ./clojure-web-hello-world
docker compose -f WordPress/docker-compose.yml build
```

Compose takes the WordPress image name from the `image` field in
[`docker-compose.yml`](WordPress/docker-compose.yml).

## Check the apps

```bash
curl http://localhost:8081         # Hello World!
curl http://localhost:8082         # Hello World
curl http://localhost:8083         # Hello, World!
curl http://localhost:8083/Docker  # Hello, Docker!
```

The Dart app reads its greeting from the `TARGET` environment variable:

```bash
docker rm -f dart-shelf
docker run -d --name dart-shelf -p 8082:8080 -e TARGET=Docker jeanjiron/dart-shelf:1.0
```

## WordPress with Docker Compose

The Compose file defines two services:

- `db`: MySQL 8.4, with a health check and a named volume for its data
- `wordpress`: the application, which starts once `db` reports healthy

Build and start the stack:

```bash
docker compose -f WordPress/docker-compose.yml up -d --build
```

Open <http://localhost:8084> and follow the WordPress installer.

Useful commands:

```bash
# Show service status
docker compose -f WordPress/docker-compose.yml ps

# Follow the application logs
docker compose -f WordPress/docker-compose.yml logs -f wordpress

# Stop the stack and keep the data
docker compose -f WordPress/docker-compose.yml down

# Stop the stack and delete the database and uploads
docker compose -f WordPress/docker-compose.yml down -v
```

The database credentials in the Compose file are for local use only. Change
them before deploying anywhere else.

## How the images are built

- **Kotlin, Dart, and Clojure** use multi-stage builds, so build tools stay out
  of the final image.
- **Kotlin and Clojure** run as a non-root `appuser` on a JRE base image.
- **Dart** compiles to a native binary on a `scratch` base image, which keeps
  the image at about 10 MB.
- **WordPress** extends `php:8.3-apache` with the `mysqli` extension and
  `mod_rewrite`.

## Publish the images

Each image is published for `linux/amd64` and `linux/arm64`. The default Docker
builder can't produce multi-platform images unless the containerd image store is
enabled, so create a `docker-container` builder once:

```bash
docker buildx create --name multiarch --driver docker-container --bootstrap
```

Then log in, and build and push each image from the repository root:

```bash
docker login
docker buildx build --builder multiarch --platform linux/amd64,linux/arm64 \
  -t jeanjiron/kotlin-ktor:1.0 --push ./ktor-get-started-sample
docker buildx build --builder multiarch --platform linux/amd64,linux/arm64 \
  -t jeanjiron/dart-shelf:1.0 --push ./cloudruntest
docker buildx build --builder multiarch --platform linux/amd64,linux/arm64 \
  -t jeanjiron/clojure-hello:1.0 --push ./clojure-web-hello-world
docker buildx build --builder multiarch --platform linux/amd64,linux/arm64 \
  -t jeanjiron/wordpress:1.0 --push ./WordPress
```

Check that a tag lists both platforms:

```bash
docker buildx imagetools inspect jeanjiron/dart-shelf:1.0
```

## Clean up

```bash
docker rm -f kotlin-ktor dart-shelf clojure-hello
docker compose -f WordPress/docker-compose.yml down -v
```

## Source projects

- [ktorio/ktor-get-started-sample](https://github.com/ktorio/ktor-get-started-sample)
- [shamblett/cloudruntest](https://github.com/shamblett/cloudruntest)
- [hjacobs/clojure-web-hello-world](https://github.com/hjacobs/clojure-web-hello-world)
- [WordPress/WordPress](https://github.com/WordPress/WordPress)
