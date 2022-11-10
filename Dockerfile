FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 42322
ADD hunger-games-backend/build/libs/hunger-games-backend.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]