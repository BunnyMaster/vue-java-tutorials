package cn.bunny.mongodb;

import cn.bunny.mongodb.dao.enity.VideoEntity;
import cn.bunny.mongodb.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class MongoDbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void testCreateCollection() {
        boolean isExist = mongoTemplate.collectionExists("free_video");
        if (isExist) {
            mongoTemplate.dropCollection("free_video");
        }
        mongoTemplate.createCollection("free_video");
    }

    @Test
    void testFindOne() {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setFree(false);
        Example<VideoEntity> example = Example.of(videoEntity);

        Optional<VideoEntity> videoRepositoryOne = videoRepository.findOne(example);
        videoRepositoryOne.ifPresent(System.out::println);
    }

    @Test
    void testFIndByCondition() {
        // 封装条件
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setDuration(0);

        // 构建条件
        Example<VideoEntity> example = Example.of(videoEntity);

        // 设置排序条件
        Sort sort = Sort.by(Sort.Direction.DESC, "playCount");

        // 查询内容
        List<VideoEntity> videoEntityList = videoRepository.findAll(example, sort);
        videoEntityList.forEach(System.out::println);
    }

    @Test
    void testFindById() {
        Optional<VideoEntity> videoRepositoryById = videoRepository.findById("6766a1148dfff0a5f60818e6");
        videoRepositoryById.ifPresent(System.out::println);
    }

    @Test
    void testFindAll() {
        List<VideoEntity> list = videoRepository.findAll();

        // 输出结果
        list.forEach(System.out::println);
    }

    @Test
    void testPage() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<VideoEntity> all = videoRepository.findAll(pageRequest);
        all.getContent().forEach(System.out::println);
    }

    @Test
    void update() {
        // 查询数据
        Optional<VideoEntity> videoRepositoryOne = videoRepository.findById("6766a1148dfff0a5f60818e5");

        // 判断数据是否存在
        if (videoRepositoryOne.isPresent()) {
            System.out.println(videoRepositoryOne.get());

            // 设置要修改的内容，之后使用save方式更新
            VideoEntity videoEntity = videoRepositoryOne.get();
            videoEntity.setTitle("爸爸生个女儿从小操");
            videoRepository.save(videoEntity);

            // 再次查询是否成功
            videoRepositoryOne = Optional.ofNullable(mongoTemplate.findOne(new Query(), VideoEntity.class));
            System.out.println(videoRepositoryOne);
        }
    }

    @Test
    void testAddOne() {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setAddTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        videoEntity.setFree(true);
        videoEntity.setDuration(555);
        videoEntity.setIsVip(false);
        videoEntity.setCoverUrl("https://jpg.byym381.cyou/20230604/oVX6enWk/1.jpg");
        videoEntity.setPlayUrl("https://xn--vcsx64d.byym325.cyou/20230604/oVX6enWk/index.m3u8");
        videoRepository.save(videoEntity);
    }

    @Test
    void testDeleted() {
        videoRepository.deleteById("xxx");
    }
}
