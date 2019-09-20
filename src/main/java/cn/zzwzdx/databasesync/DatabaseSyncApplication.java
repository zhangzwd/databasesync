package cn.zzwzdx.databasesync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@MapperScan({
    "hk.ejs.daolan.base.dao"
        ,"cn.zzwzdx.databasesync.dao"
})
@Import({
    hk.ejs.BeanDefinition.CommonBean.class
    ,hk.ejs.util.VariableUtils.class
    ,hk.ejs.util.ConfigUtils.class
})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DatabaseSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseSyncApplication.class, args);
    }

}
