package cn.zzwzdx.databasesync.service;

import cn.zzwzdx.databasesync.annotion.DS;
import cn.zzwzdx.databasesync.config.DatabaseType;
import cn.zzwzdx.databasesync.dao.IAlbumDao4Sync;
import hk.ejs.daolan.base.constant.DataState;
import hk.ejs.daolan.base.domain.Album;
import hk.ejs.daolan.base.domain.AlbumEntity;
import hk.ejs.daolan.base.service.AlbumService;
import hk.ejs.exception.MyBizLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlbumService4Sync extends AlbumService {

    @Resource
    private IAlbumDao4Sync albumDao4Sync;

    @DS(DatabaseType.devDataSource)
    public List<Album> getAllAlbum(long inid){
        Map<String, Serializable> condition = new HashMap<>(3);
        condition.put("inid",inid);
        condition.put("state", DataState.Published.getCode());
        condition.put("mark",0);
        List<Album> albums = this.getListByCondition(condition);
        System.out.println(albums);
        return albums;
    }

    @Transactional(rollbackFor = Exception.class)
    @DS(DatabaseType.onlineDataSource)
    public void saveAlbumToOnlineDataBase(List<Album> albums,long inid) throws MyBizLogicException {
        //清除
//        albumDao4Sync.deleteAll(inid);

        if(!CollectionUtils.isEmpty(albums)){
            List<AlbumEntity> aes = albums.stream().map(album -> {
                AlbumEntity ae = new AlbumEntity();
                BeanUtils.copyProperties(album, ae);
                ae.setInid(inid);
                return ae;
            }).filter(ae->ae.getState() == DataState.Published.getCode())
            .collect(Collectors.toList());

            for (AlbumEntity ae: aes){
                this.createEntity(ae);
            }
        }else {
            log.info("开发环境中没有album数据");
        }

    }


    public void syncAlbum(long devInid,long onlineInid) throws MyBizLogicException {
        AlbumService4Sync albumService4Sync = (AlbumService4Sync)AopContext.currentProxy();
        List<Album> allAlbum = albumService4Sync.getAllAlbum(devInid);
        albumService4Sync.saveAlbumToOnlineDataBase(allAlbum,onlineInid);
    }

}
