#!/bin/bash

# run from project root

# down everything

# cd kafka
# sudo docker compose down
# cd ..

# cd monitoring
# sudo docker compose down
# cd ..

# cd hadoop
# sudo docker compose down
# cd ..

# cd cassandra
# sudo docker compose down
# cd ..

# cd server
# sudo docker compose down
# cd ..

# cd front
# sudo docker compose down
# cd ..

# now up everything

docker network create app_out_net
docker network create cassandra_out_net

cd kafka
sudo docker compose up -d
cd ..

cd monitoring
sudo docker compose up -d
cd ..

cd hadoop
sudo docker compose up -d
cd ..

cd cassandra
sudo docker compose up -d
cd ..

cd server
sudo docker compose up -d
cd ..

cd front
sudo docker compose up -d
cd ..
