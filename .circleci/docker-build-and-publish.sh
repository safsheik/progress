#!/bin/bash
DOCKERFILE=$1
if [ ! -z $DOCKERFILE ];
then
  if [ -r $DOCKERFILE ]; 
  then
    echo Using $DOCKERFILE for creating Docker Container...;
    cp $DOCKERFILE ./Dockerfile ;
  else
    echo Cannot read file: $DOCKERFILE
    exit 1;
  fi
fi
docker build --cache-from registry.heroku.com/$HEROKU_APP_NAME/web:latest -t registry.heroku.com/$HEROKU_APP_NAME/web .
docker login -u $HEROKU_LOGIN -p $HEROKU_TOKEN registry.heroku.com
docker push registry.heroku.com/$HEROKU_APP_NAME/web:latest