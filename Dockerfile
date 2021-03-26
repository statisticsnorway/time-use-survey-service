FROM azul/zulu-openjdk:15

# Add the service itself
COPY ./target/timeuse-survey-service.jar /usr/share/timeuse/

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "/usr/share/timeuse/timeuse-survey-service.jar"]

EXPOSE 9000

