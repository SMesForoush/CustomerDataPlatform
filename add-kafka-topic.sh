dockerId=$1
# kafka1:9092
kafkaHost=$2
topicName=$3
topicPartition=$4
topicReplication=$5

docker exec -i $dockerId kafka-topics --create --bootstrap-server $kafkaHost --replication-factor $topicReplication --partitions $topicPartition --topic $topicName
