package com.lee.hbasedemo.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HbaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(HbaseDAO.class);

    private static final Configuration conf = HBaseConfiguration.create();

    public static Connection connection;

    static {
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if (connection != null) {
            connection.close();
            logger.info("HBASE CONNECTION CLOSED");
        }
    }

    public static void main(String[] args) {

    }

}
