FROM openjdk:11

MAINTAINER Marco Vermeulen

RUN mkdir /broker

ADD build/libs/sdkman-broker-*-all.jar /broker/sdkman-broker.jar

ENTRYPOINT java -Xmx128m -XX:+PrintFlagsFinal -jar /broker/sdkman-broker.jar
