FROM openjdk:12-alpine
VOLUME /tmp
COPY './build/libs/gshow*.jar' app.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
