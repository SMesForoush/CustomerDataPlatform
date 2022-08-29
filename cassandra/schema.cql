CREATE KEYSPACE IF NOT EXISTS tfidf
            WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

CREATE TABLE IF NOT EXISTS tfidf.tf
(
    word     text,
    document text,
    tf       int,
    PRIMARY KEY ((word, document), tf)
)
    WITH comment = 'Find tf of the word in a document' AND CLUSTERING ORDER BY (tf DESC);

CREATE TABLE IF NOT EXISTS tfidf.df
(
    word text,
    df   int,
    PRIMARY KEY ((word), df)
)
            WITH comment = 'Find df of the word' AND CLUSTERING ORDER BY (df DESC);

CREATE TABLE IF NOT EXISTS tfidf.config
(
    id             int,
    totalDocuments int,
    PRIMARY KEY ((id), totalDocuments)
)
    WITH comment = 'A one row table to store totalDocuments after each stream'
     AND CLUSTERING ORDER BY (totalDocuments DESC);
