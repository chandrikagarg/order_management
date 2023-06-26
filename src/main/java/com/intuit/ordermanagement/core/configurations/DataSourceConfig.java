//package com.intuit.ordermanagement.core.configurations;
//
//import com.zaxxer.hikari.HikariConfig;
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories()
//public class DataSourceConfig extends HikariConfig {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/order_management";
//    private static final String DB_USERNAME = "root";
//    private static final String DB_PASSWORD = "chandrika123";
//
//    public static DataSource getDataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl(DB_URL);
//        dataSource.setUsername(DB_USERNAME);
//        dataSource.setPassword(DB_PASSWORD);
//
//        // Set other optional configuration properties
//        dataSource.setInitialSize(5);
//        dataSource.setMaxTotal(10);
//
//        return dataSource;
//    }
//}
