package cn.zzwzdx.databasesync.aop;

import cn.zzwzdx.databasesync.annotion.DS;
import cn.zzwzdx.databasesync.config.DatabaseContextHolder;
import cn.zzwzdx.databasesync.config.DatabaseType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@Order(Ordered.LOWEST_PRECEDENCE-1)
public class DataSourceAspect {

    /**
     * 通知
     * @param point
     */
    @Before("@annotation(cn.zzwzdx.databasesync.annotion.DS)")
    public void before(JoinPoint point) {
        // 获得访问的方法名称
        String methodName = point.getSignature().getName();
        log.info("拦截到了{}方法",methodName);
        DatabaseType dataSourceType = DatabaseContextHolder.DEFAULT_DS;
        try {
            //获得访问的方法对象
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            // 判断是否有@DS注解
            if(method.isAnnotationPresent(DS.class)){
                DS annotation = method.getAnnotation(DS.class);
                // 取出注解中的数据源
                dataSourceType = annotation.value();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("设置数据源失败：{}",e);
        }
        DatabaseContextHolder.setDatabaseType(dataSourceType);
    }

    @AfterReturning("@annotation(cn.zzwzdx.databasesync.annotion.DS)")
    public void afterSwitchDS(JoinPoint point){
        DatabaseContextHolder.clearDatabaseType();
    }

}
