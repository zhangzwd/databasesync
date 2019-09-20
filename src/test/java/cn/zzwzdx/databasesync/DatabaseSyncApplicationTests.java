package cn.zzwzdx.databasesync;

import cn.zzwzdx.databasesync.service.AlbumService4Sync;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseSyncApplicationTests {
    @Autowired
    private AlbumService4Sync albumService4Sync;

    //知音号数据同步
    long devInid = 954904680541196288L;
    long onlineInid = 1143354745487101952L;


    @Test
    public void syncAlbum() throws Exception {
        albumService4Sync.syncAlbum(devInid,onlineInid);
    }

}
