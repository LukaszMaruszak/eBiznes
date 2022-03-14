FROM ubuntu:20.04

RUN apt-get update && apt-get install -y

#Java
RUN apt-get install openjdk-11-jdk -y

RUN apt-get install curl zip unzip -y

#Kotlin
RUN curl -s https://get.sdkman.io | bash
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin"

#Kotlin liner ktlint
RUN curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.44.0/ktlint && \
	chmod a+x ktlint && \
	mv ktlint /usr/local/bin/
