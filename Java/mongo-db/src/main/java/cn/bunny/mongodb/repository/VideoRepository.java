package cn.bunny.mongodb.repository;

import cn.bunny.mongodb.dao.enity.VideoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<VideoEntity, String> {

}
