FROM openjdk:15-alpine

RUN mkdir /app
RUN mkdir /app/resources
COPY ./build/libs/ttrpg-api-1-all.jar /app/ttrpg-api-1-all.jar
COPY ./resources/hikari.properties /app/resources/hikari.properties
WORKDIR /app

CMD ["java", "-jar", "ttrpg-api-1-all.jar"]