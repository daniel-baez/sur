# Pull base image.
# FROM ubuntu:17.10
FROM java:8

# Install.

RUN \
  apt-get update && \
  apt-get install -y zip && \
  rm -rf /var/lib/apt/lists/*

RUN curl -o- https://raw.githubusercontent.com/daplay/sur/master/install.sh | bash

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Define default command.
CMD ["bash"]
