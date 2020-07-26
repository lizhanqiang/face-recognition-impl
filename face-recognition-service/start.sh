#!/usr/bin/sh


ps aux | grep face-recognition-service | grep -v grep | awk '{print $2}' | xargs kill -9

nohup java -jar target/face-recognition-service-1.0-SNAPSHOT.jar >service.log  2>&1 &
