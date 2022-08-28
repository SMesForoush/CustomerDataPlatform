from hdfs import InsecureClient
from json import dumps

if __name__ == '__main__':
    client = InsecureClient('http://localhost:50070', user='root')
    records = {'name': 'test', 'num': 2}
    client.write('data/records.json', data=dumps(records), encoding='utf-8')
#   below code us context manager: 
#    with client.write('test.json', encoding='utf-8') as writer:
#        dump(records, writer)


"""
if you want to write a pandas dataframe you can use this code :  

    hdfs.ext.dataframe.write_dataframe(client, hdfs_path, df, **kwargs)
        client : hdfs.client.client instance
        hdfs_path : remote path where dataframe will be stored
        df : dataframe to store
        **kwargs: Keyword arguments passed through to hdfs.ext.avro.AvroWriter.

----------------------------------------------------------
    you need Avro and pandas extensions to be installed
    for more information check :
        https://hdfscli.readthedocs.io/en/latest/api.html#hdfs.client.Client.write
"""