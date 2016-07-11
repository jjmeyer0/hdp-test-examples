package com.jj.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseClient {
    private Connection connection;

    public HBaseClient(Connection connection) {
        this.connection = connection;
    }

    public void insert(String table, String row, Cell item) throws IOException {
        insert(connection.getTable(TableName.valueOf(table)), row, item);
    }

    public void insert(Table table, String row, Cell item) throws IOException {
        Put put = new Put(Bytes.toBytes(row));
        put.add(item);
        table.put(put);
    }

    public byte[] find(String tableName, String row) throws IOException {
        return find(connection.getTable(TableName.valueOf(tableName)), row);
    }
    public byte[] find(Table table, String row) throws IOException {
        return null;
    }

    public void delete(String tableName, String row) throws IOException {
        delete(connection.getTable(TableName.valueOf(tableName)), row);
    }

    public void delete(Table table, String row) throws IOException {
        Delete delete = new Delete(Bytes.toBytes(row));
        table.delete(delete);
    }
}
