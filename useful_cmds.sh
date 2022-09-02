dcu() {
    sudo docker-compose up
}

upinfconf() {
    CDPK="$HOME/CustomerDataPlatform/kafka"

    sh "$CDPK/scripts/add-new-config-to-kafka-connect.sh" "$CDPK/connector_config/influxdb-sink-connector.json" 8083
}

influxshell() {
    sudo docker exec -it monitoring_influxdb_1 influx -username admin -password admin
}

connectlog() {
    sudo docker logs kafka_kafka-connect_1 -f
}

kafkaproducer() {
    TOPIC=$1
    sudo docker exec -it kafka_kafka1_1 kafka-console-producer  --bootstrap-server kafka1:29092  --topic $TOPIC 
}

kafkaconsumer() {
    TOPIC=$1
    sudo docker exec -it kafka_kafka1_1 kafka-console-consumer --bootstrap-server kafka1:29092  --topic $TOPIC --from-beginning
}

delinfconf() {
    curl -X DELETE "http://localhost:8083/connectors/InfluxDBSinkConnector"
}

restartinfconf() {
    curl -X POST "http://localhost:8083/connectors/InfluxDBSinkConnector/restart"
}

restart_influx_connector_task() {
    curl -X POST "http://localhost:8083/connectors/InfluxDBSinkConnector/tasks/0/restart"
}