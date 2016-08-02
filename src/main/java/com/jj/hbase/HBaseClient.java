package com.jj.hbase;

import com.google.common.collect.Sets;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class HBaseClient {
    private Connection connection;

    public HBaseClient(Connection connection) {
        this.connection = connection;
    }

    public void insert(String table, Map<String, ColumnFamily> columnFamilies) throws IOException {
        try (Table t = connection.getTable(TableName.valueOf(table))) {
            List<Put> puts = new ArrayList<>(columnFamilies.size());
            columnFamilies.forEach((rowKey, columnFamily) -> {
                final byte[] family = Bytes.toBytes(columnFamily.getName());
                Put put = new Put(Bytes.toBytes(rowKey));
                columnFamily.getColumns().forEach((column, value) -> put.addColumn(family, Bytes.toBytes(column), Bytes.toBytes(value)));
                puts.add(put);
            });

            t.put(puts);
        }
    }

    public void insert(String table, String rowKey, ColumnFamily columnFamily) throws IOException {
        try (Table t = connection.getTable(TableName.valueOf(table))) {
            final byte[] family = Bytes.toBytes(columnFamily.getName());

            Put put = new Put(Bytes.toBytes(rowKey));
            columnFamily.getColumns().forEach((column, value) -> put.addColumn(family, Bytes.toBytes(column), Bytes.toBytes(value)));

            t.put(put);
        }
    }

    public Result find(String tableName, String rowKey, String columnFamily, Set<String> columns) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            final byte[] family = Bytes.toBytes(columnFamily);
            Get get = new Get(Bytes.toBytes(rowKey));

            columns.forEach(c -> get.addColumn(family, Bytes.toBytes(c)));

            return table.get(get);
        }
    }

    public void delete(String tableName, String rowKey) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))){
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        }
    }

    public void delete(String tableName, String rowKey, String columnFamily) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))){
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addFamily(Bytes.toBytes(columnFamily));

            table.delete(delete);
        }
    }

    public static void main(String[] args) throws IOException {
        // Setting up the HBase configuration
        Configuration configuration = new Configuration();
        configuration.addResource("src/main/resources/hbase-site.xml");

        // Point to the krb5.conf file. Alternatively this could be setup when running the program using: -Djava.security.krb5.conf=<full path to krb5.conf>
        System.setProperty("java.security.krb5.conf", "src/main/resources/krb5.conf");
        System.setProperty("sun.security.krb5.debug", "true");

        String principal = System.getProperty("kerberosPrincipal", "jj@EXAMPLE.COM");
        String keytabLocation = System.getProperty("kerberosKeytab", "src/main/resources/jj.keytab");

        UserGroupInformation.setConfiguration(configuration);
        UserGroupInformation.loginUserFromKeytab(principal, keytabLocation);

        HBaseClient client = new HBaseClient(new ConnectionGetter(configuration).getConnection());
        client.insert("test", "test-java-1", new ColumnFamily("test-family").addColumn("col1", "val-java-1"));
        System.out.println(client.find("test", "test-1", "test-family", Sets.newHashSet("col1")));
    }
}