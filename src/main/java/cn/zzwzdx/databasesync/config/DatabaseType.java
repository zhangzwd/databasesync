package cn.zzwzdx.databasesync.config;

/**
 * 列出数据源类型
 */
public enum DatabaseType {
    devDataSource("devDataSource"),
    onlineDataSource("onlineDataSource");

    DatabaseType(String name){
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DatabaseType{" +
                "name='" + name + '\'' +
                '}';
    }
}
