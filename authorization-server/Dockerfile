FROM eclipse-temurin:21-jre
RUN mkdir "/opt/library"
COPY "target/authorization-server-0.0.1-SNAPSHOT.jar" "/opt/library/auth-server.jar"
CMD ["java", "-jar", "/opt/library/auth-server.jar"]
EXPOSE 9000