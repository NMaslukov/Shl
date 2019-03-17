# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="dudoser228@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8079 available to the world outside this container
EXPOSE 8079

# The application's jar file
ARG JAR_FILE=target/Shala-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} Shala-0.0.1-SNAPSHOT.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/Shala-0.0.1-SNAPSHOT.jar"]