#!/usr/bin/env bash

if [ "$1" = "native" ];
then
  ./mvnw clean spring-boot:build-image -DskipTests --projects simple-service
else
  ./mvnw clean compile jib:dockerBuild -DskipTests --projects simple-service
fi