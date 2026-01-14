package cn.bunny.mongodb;

import cn.bunny.mongodb.dao.enity.VideoEntity;
import com.mongodb.client.result.DeleteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@SpringBootTest
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testAdd() {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setFree(false);
        videoEntity.setVideoTag("5555");

        mongoTemplate.insert(videoEntity);
    }

    @Test
    void testFindAll() {
        List<VideoEntity> videoEntityList = mongoTemplate.findAll(VideoEntity.class);
        videoEntityList.forEach(System.out::println);
    }

    @Test
    void testFindById() {
        VideoEntity videoEntity = mongoTemplate.findById("6766a1148dfff0a5f60818ea", VideoEntity.class);
        System.out.println(videoEntity);
    }

    @Test
    void testFIndByCondition() {
        Criteria criteria = Criteria.where("title").is("罗智莹523")
                .and("addTime").gte("11月05日");
        Query query = new Query(criteria);

        List<VideoEntity> videoEntities = mongoTemplate.find(query, VideoEntity.class);
        videoEntities.forEach(System.out::println);
    }

    @Test
    void testPage() {
        Query query = new Query();
        query.skip(0).limit(15);
        List<VideoEntity> videoEntities = mongoTemplate.find(query, VideoEntity.class);
        videoEntities.forEach(System.out::println);
    }

    @Test
    void testUpdate() {
        Criteria criteria = Criteria.where("_id").is("6766a1148dfff0a5f60818e5");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("dayPlayCount", "666");
        update.set("videoTag", "好看");
        mongoTemplate.upsert(query, update, VideoEntity.class);
    }

    @Test
    void testDeleted() {
        Criteria criteria = Criteria.where("_id").is("6766a1148dfff0a5f60818e5");
        Query query = new Query(criteria);

        List<VideoEntity> videoEntities = mongoTemplate.find(query, VideoEntity.class);
        videoEntities.forEach(System.out::println);

        DeleteResult result = mongoTemplate.remove(query, VideoEntity.class);
        System.out.println(result.getDeletedCount());
    }
}
