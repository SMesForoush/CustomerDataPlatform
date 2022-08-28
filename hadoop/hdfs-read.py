from hdfs import InsecureClient

if __name__ == '__main__':
    client = InsecureClient('http://localhost:50070', user='root')
    with client.read('data/records.json') as reader:
        features = reader.read()
        print(features)


"""
if you want to read a pandas dataframe you can use this code :  

    hdfs.ext.dataframe.read_dataframe(client, hdfs_path)
        client : hdfs.client.client instance
        hdfs_path : remote path to avro file

----------------------------------------------------------
    you need Avro and pandas extensions to be installed
    for more information check :
        https://hdfscli.readthedocs.io/en/latest/api.html#hdfs.client.Client.write
"""