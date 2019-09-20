package cn.zzwzdx.databasesync.controller;

import cn.zzwzdx.databasesync.service.AlbumService4Sync;
import hk.ejs.exception.MyBizLogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private AlbumService4Sync albumService4Sync;
    @Autowired
    private Environment env;

    @RequestMapping("/sync")
    public String albumSync() throws MyBizLogicException {
        long devInid = Long.valueOf(env.getProperty("devInid"));
        long onlineInid = Long.valueOf(env.getProperty("onlineInid"));
        albumService4Sync.syncAlbum(devInid,onlineInid);
        return "SYNC SUCCESS";
    }
}
