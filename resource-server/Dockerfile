FROM eclipse-temurin:21-jre
RUN mkdir "/opt/library"
COPY "target/resource-server-0.0.1-SNAPSHOT.jar" "/opt/library/resource-server.jar"
CMD ["java", "-jar", "/opt/library/resource-server.jar"]
EXPOSE 8080