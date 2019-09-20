package cn.zzwzdx.databasesync.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAlbumDao4Sync{

    /**
     * 删除指定单位的音频数据
     * @param inid
     */
    @Delete("delete * from album where inid = #{inid}")
    void deleteAll(long inid);

}
