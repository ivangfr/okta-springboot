#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

SIMPLE_SERVICE_APP_NAME="simple-service"
SIMPLE_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${SIMPLE_SERVICE_APP_NAME}:${APP_VERSION}"

SKIP_TESTS="true"

./mvnw clean compile jib:dockerBuild \
  --projects "$SIMPLE_SERVICE_APP_NAME" \
  -DskipTests="$SKIP_TESTS" \
  -Dimage="$SIMPLE_SERVICE_DOCKER_IMAGE_NAME"