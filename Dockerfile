FROM openjdk:11

MAINTAINER Marco Vermeulen

RUN mkdir /broker

ADD build/libs /broker

ENTRYPOINT java -Xmx128m -XX:+PrintFlagsFinal -jar /broker/sdkman-broker-1.0.0-SNAPSHOT-all.jar
