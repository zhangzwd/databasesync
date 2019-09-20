package cn.zzwzdx.databasesync.config;

/**
 * 保存一个线程安全的DatabaseType容器
 */
public class DatabaseContextHolder {
    /**
     * 默认数据源
     */
    public static final DatabaseType DEFAULT_DS = DatabaseType.devDataSource;

    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();

    public static void setDatabaseType(DatabaseType type) {
        contextHolder.set(type);
    }

    public static DatabaseType getDatabaseType() {
        return contextHolder.get();
    }

    // 清除数据源名
    public static void clearDatabaseType() {
        contextHolder.remove();
    }


}
