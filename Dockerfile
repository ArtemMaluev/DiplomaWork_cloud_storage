FROM openjdk:11
VOLUME /tmp
EXPOSE 8888
ADD target/cloud_storage_backend-0.0.1-SNAPSHOT.jar app-backend-cloud.jar
ENTRYPOINT ["java", "-jar", "/app-backend-cloud.jar"]