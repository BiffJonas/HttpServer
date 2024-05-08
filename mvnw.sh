#!/usr/bin/env bash
mvn package -DskipTests
java -cp target/HttpServer-1.0-SNAPSHOT.jar com.httpserver.server.App
