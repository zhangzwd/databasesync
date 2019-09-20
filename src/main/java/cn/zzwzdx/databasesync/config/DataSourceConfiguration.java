package cn.zzwzdx.databasesync.config;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import hk.ejs.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义数据源
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {
    @Autowired
    private Environment env;



//    方式一:
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.dev")
//    public DataSourceProperties devDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.online")
//    public DataSourceProperties onlineDataSourceProperties() {
//        return new DataSourceProperties();
//    }


//    @Bean(name = "devDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.dev")
//    @Primary
//    public HikariDataSource devDataSource(@Qualifier("devDataSourceProperties") DataSourceProperties properties) {
//        HikariDataSource datasource = properties.initializeDataSourceBuilder().type(HikariDataSource.class)
//                .build();
//        return datasource;
//    }
//
//
//    @Bean(name = "onlineDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.online")
//    public DataSource onlineDataSource(@Qualifier("onlineDataSourceProperties") DataSourceProperties properties) {
//        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
//                .build();
//    }

    @Bean
    public Gson gson(){
        return new Gson();
    }


//  方式二:
    @Bean("devDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.dev")
    public HikariDataSource devDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("onlineDataSource")
    @ConfigurationProperties("spring.datasource.online")
    public HikariDataSource onlineDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


    /**
     *  构造多数据源连接池
     * @param dev 数据源连接池采用 HikariDataSource
     * @param online 数据源连接池采用 HikariDataSource
     * @return
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("devDataSource") DataSource dev, @Qualifier("onlineDataSource") DataSource online) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DatabaseType.devDataSource, dev);
        targetDataSources.put(DatabaseType.onlineDataSource, online);

        DynamicDataSource dataSource = new DynamicDataSource();
        // 该方法是AbstractRoutingDataSource的方法
        dataSource.setTargetDataSources(targetDataSources);
        // 默认的datasource设置为devDataSource
        dataSource.setDefaultTargetDataSource(dev);

//        String read = env.getProperty("spring.datasource.read");
//        dataSource.setMethodType(DatabaseType.dev, read);
//
//        String write = env.getProperty("spring.datasource.write");
//        dataSource.setMethodType(DatabaseType.online, write);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("devDataSource") DataSource dev, @Qualifier("onlineDataSource") DataSource online) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dataSource(dev, online));
//        fb.setTypeAliasesPackage(env.getProperty("mybatis.type-aliases-package"));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapper-locations")));
        return fb.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }


}
