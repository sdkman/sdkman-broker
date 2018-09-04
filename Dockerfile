FROM openjdk:8

MAINTAINER Marco Vermeulen

RUN mkdir /broker

ADD build/libs /broker

ENTRYPOINT java -Xmx128m -XX:+PrintFlagsFinal -XX:+UseConcMarkSweepGC -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /broker/sdkman-broker-1.0.0-SNAPSHOT-all.jar
