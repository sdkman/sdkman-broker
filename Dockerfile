FROM java:8

MAINTAINER Marco Vermeulen

RUN mkdir /broker

ADD build/libs /broker

ENTRYPOINT java -jar /broker/sdkman-broker-all.jar
