package cn.zzwzdx.databasesync.annotion;

import cn.zzwzdx.databasesync.config.DatabaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DS {
    DatabaseType value() default DatabaseType.devDataSource;
}
