#!/usr/bin/env bash

set -e

./gradlew clean assemble

docker-compose up --build
