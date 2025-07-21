#!/bin/bash

# 환경 변수 설정
MYSQL_IMAGE="mysql:8.0"
CONTAINER_NAME="mysql-db"
MYSQL_ROOT_PASSWORD="root_password"
MYSQL_DATABASE="local_buzz"
MYSQL_USER="dev_user"
MYSQL_PASSWORD="dev_password"
HOST_PORT=3306
CONTAINER_PORT=3306
VOLUME_NAME="db_data"

# Docker 볼륨 생성
docker volume create $VOLUME_NAME

# MySQL 컨테이너 실행
docker run -d \
  --name $CONTAINER_NAME \
  -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
  -e MYSQL_DATABASE=$MYSQL_DATABASE \
  -e MYSQL_USER=$MYSQL_USER \
  -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
  -p $HOST_PORT:$CONTAINER_PORT \
  -v $VOLUME_NAME:/var/lib/mysql \
  --restart always \
  $MYSQL_IMAGE