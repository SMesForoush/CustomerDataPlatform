influx -execute "CREATE RETENTION POLICY one_day_only ON myorg DURATION 1d REPLICATION 1 DEFAULT"