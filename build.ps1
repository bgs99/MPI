$ErrorActionPreference = "Stop"

./gradlew clean assemble

docker-compose up --build
