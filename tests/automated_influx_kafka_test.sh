# run this from project root

cd kafka
sudo docker-compose down
cd ..

cd monitoring
sudo docker-compose down
cd ..

cd kafka
sudo docker-compose up &
cd ..

cd monitoring
sudo docker-compose up &
cd ..