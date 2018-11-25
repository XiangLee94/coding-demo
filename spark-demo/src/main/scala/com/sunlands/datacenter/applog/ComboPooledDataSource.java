package com.sunlands.datacenter.applog;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;

import javax.naming.Referenceable;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;

public final class ComboPooledDataSource extends AbstractComboPooledDataSource implements Serializable , Referenceable {

    public ComboPooledDataSource(){
        super();
    }

    public ComboPooledDataSource(boolean autoregister){
        super(autoregister);
    }

    public ComboPooledDataSource(String configName){
        super(configName);
    }

    private static final long serialVersionUID = 1l;

    private static final short VERSION = 0x0002;

    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.writeObject(VERSION);
    }

}

//class JDBCUtils{
//    public  ComboPooledDataSource getC3p0DateSource(String filename,String config) throws IOException, PropertyVetoException {
//        ComboPooledDataSource  dataSource= new ComboPooledDataSource(true);
//        Properties prop =new Properties();
//        FileInputStream fis = new FileInputStream("prop.properties");
//        prop.load(fis);
//        dataSource.setJdbcUrl(prop.getProperty("url"));
//        dataSource.setDriverClass(prop.getProperty("driverClassName"));
//        dataSource.setUser(prop.getProperty("username"));
//        dataSource.setPassword(prop.getProperty("password"));
//        dataSource.setMaxPoolSize(Integer.valueOf(prop.getProperty("maxPoolSize")));
//        dataSource.setMinPoolSize(Integer.valueOf(prop.getProperty("minPoolSize")));
//        dataSource.setAcquireIncrement(Integer.valueOf(prop.getProperty("acquireIncrement")));
//        dataSource.setInitialPoolSize(Integer.valueOf(prop.getProperty("initialPoolSize")));
//        dataSource.setMaxIdleTime(Integer.valueOf(prop.getProperty("maxIdleTime")));
//        return dataSource;
//    }
//}
