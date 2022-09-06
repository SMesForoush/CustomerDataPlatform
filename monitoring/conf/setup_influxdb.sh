influx -execute "CREATE RETENTION POLICY one_day_only ON myorg DURATION 1h REPLICATION 1 DEFAULT"
influx -execute "CREATE CONTINUOUS QUERY counting_course_users ON myorg BEGIN SELECT count(DISTINCT(user_id)) INTO counted_course_users FROM myorg_topic GROUP BY time(2s), course_id END"
influx -execute "CREATE CONTINUOUS QUERY event_counting ON myorg BEGIN SELECT count(*) INTO counted_events FROM myorg_topic group by time(2s), event_place END"
influx -execute "CREATE CONTINUOUS QUERY counting_users ON myorg BEGIN SELECT count(DISTINCT(user_id)) INTO counted_users FROM myorg_topic  GROUP BY time(30s) END"
