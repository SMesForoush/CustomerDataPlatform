CREATE KEYSPACE IF NOT EXISTS myorg
            WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

CREATE TABLE IF NOT EXISTS myorg.online_users
(
    event_time  time,
    event_date date,
    user_id  text,
    PRIMARY KEY (event_date, event_time, user_id)
)
    WITH comment = 'Find number of online users in a given date and time' AND CLUSTERING ORDER BY (event_time DESC);

CREATE TABLE IF NOT EXISTS myorg.online_users_by_course_time
(
    event_time  time,
    event_date date,
    user_id  text,
    PRIMARY KEY ((course_id, event_date), event_time, user_id)
)
    WITH comment = 'Find number of online users in a given date and time and course' AND CLUSTERING ORDER BY (event_time ASC);


CREATE TABLE IF NOT EXISTS myorg.click_by_place
(
    click_place text,
    click_date date,
    click_time time, 
    totalDocuments int,
    PRIMARY KEY ((click_place, click_date), click_time)
)
    WITH comment = 'table for finding number of clicks per click place in a given time'
     AND CLUSTERING ORDER BY (click_time ASC);
