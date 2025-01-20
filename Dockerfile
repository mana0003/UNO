# Use an official Scala image as a parent image
FROM hseeberger/scala-sbt:11.0.12_1.5.5_2.13.6

# Update and install additional dependencies
RUN apt-get update && \
    apt-get install -y libxrender1 libxtst6 libxi6

# Set the working directory in the container
WORKDIR /uno

# Copy the current directory contents into the container at /uno
COPY . /uno

# Install sbt dependencies and build the project
RUN sbt compile

# Expose the port the application runs on
EXPOSE 8080

# Run the application
CMD ["sbt", "run"]