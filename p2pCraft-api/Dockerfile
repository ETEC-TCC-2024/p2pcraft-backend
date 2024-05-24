FROM gradle:8.7.0-jdk-graal as BUILD
ENV APP_PATH=/usr/app/
WORKDIR $APP_PATH
COPY build.gradle.kts $APP_PATH
COPY src src
EXPOSE 8080

ENTRYPOINT gradle bootRun & gradle -t classes
