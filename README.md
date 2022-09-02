# CustomerDataPlatform

## creating an organization

1. create keyspace and related schemas in cassandra (using schema.cql)
2. create connector configs for cassandra and influx relative to new org with neworg topic
3. import new configs to kafka connect
4. create a new dashboard for new user with specific previliges for new influx data source 
5. create a user pass for web ui
6. import new data to kafka