FROM node:14.11.0-slim AS web-build
COPY ./app-web /build/web
WORKDIR /build/web
RUN npm install
RUN npm run build

FROM gradle:6.6.1-jdk11 AS server-build
COPY --chown=gradle:gradle . /build/server
WORKDIR /build/server
COPY --from=web-build /build/web/build /build/server/src/main/resources/public
RUN gradle bootJar

FROM openjdk:12-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=server-build /build/server/build/libs/*.jar /app/app.jar
ENV PORT 8080 
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${PORT}", "-jar","/app/app.jar"]
