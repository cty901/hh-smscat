package com.hh.smscat.base.dbchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 2018/5/23 15:11
 *
 * @author owen pan
 */
@Configuration
@EnableTransactionManagement
public class DSConfig {
    private static final Logger logger = LoggerFactory.getLogger(DSConfig.class);
    @Autowired
    private TomcatDSProperty dataSourceProperty;


    @Primary
    @Bean(name = DSNames.MasterDataSource)
    public DataSource masterDataSource(@Value("${spring.datasource.url}")
                                               String dbUrl,
                                       @Value("${spring.datasource.username}")
                                               String username,
                                       @Value("${spring.datasource.password}")
                                               String password,
                                       @Value("${spring.datasource.driver-class-name}")
                                               String driverClassName,
                                       @Value("${spring.datasource.hikari.connection-test-query}")
                                               String validationQuery) {
        DynamicDataSourceHolder.setDefaultDataSource(DSNames.MasterDataSource);
        return dataSourceProperty.newTomcatDatasource(dbUrl, username, password, driverClassName, validationQuery);
    }

    @Bean(name = DSNames.ClusterDataSource)
    public DataSource clusterDataSource(@Value("${custom.datasource.ds1.url}")
                                                String dbUrl,
                                        @Value("${custom.datasource.ds1.username}")
                                                String username,
                                        @Value("${custom.datasource.ds1.password}")
                                                String password,
                                        @Value("${custom.datasource.ds1.driver-class-name}")
                                                String driverClassName,
                                        @Value("${spring.datasource.ds1.hikari.connection-test-query}")
                                                String validationQuery) {
        DynamicDataSourceHolder.setDefaultDataSource(DSNames.ClusterDataSource);
        return dataSourceProperty.newTomcatDatasource(dbUrl, username, password, driverClassName, validationQuery);
    }
}