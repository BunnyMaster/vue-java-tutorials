package cn.bunny.mongodb;

import cn.bunny.mongodb.dao.enity.Response;
import cn.bunny.mongodb.dao.enity.VideoEntity;
import cn.bunny.mongodb.utils.HttpRequestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@SpringBootTest
public class VideoSaveTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @SneakyThrows
    @Test
    void saveVideResponseData() {
        String url = "https://mjfh136.cyou/view/videoList/1637462276570050562/" + 7 + "/80";
        HttpRequestUtils<Response<VideoEntity>> requestUtils = new HttpRequestUtils<>();
        Response<VideoEntity> responseData = requestUtils.requestGET(url);

        List<VideoEntity> list = responseData.getData().getList();

        mongoTemplate.insertAll(list);
    }
}
