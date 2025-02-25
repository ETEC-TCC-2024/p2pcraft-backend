FROM bellsoft/liberica-openjre-alpine:22 AS layertools
WORKDIR /extract

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract --destination /extract/

FROM bellsoft/liberica-openjre-alpine:22 AS runtime
WORKDIR /app

COPY --from=layertools /extract/dependencies/ ./
COPY --from=layertools /extract/spring-boot-loader/ ./
COPY --from=layertools /extract/snapshot-dependencies/ ./
COPY --from=layertools /extract/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.aot.enabled=true", "org.springframework.boot.loader.launch.JarLauncher"]