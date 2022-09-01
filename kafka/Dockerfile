FROM docker.repos.balad.ir/confluentinc/cp-kafka-connect-base:7.0.5
RUN curl -O -L "https://downloads.datastax.com/kafka/kafka-connect-cassandra-sink.tar.gz"
RUN mkdir datastax-connector
RUN tar xzf kafka-connect-cassandra-sink.tar.gz -C datastax-connector --strip-components=1
RUN mkdir -p /usr/share/java/kafka-connect-cassandra/
RUN mv datastax-connector/kafka-connect* /usr/share/java/kafka-connect-cassandra/
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-hdfs:10.1.12
