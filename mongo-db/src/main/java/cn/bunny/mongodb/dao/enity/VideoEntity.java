package cn.bunny.mongodb.dao.enity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "xhl_video")
public class VideoEntity {

    @Id
    private ObjectId _id;

    @Field("videoId")
    private String id;

    private String title;

    private String videoTag;

    private String coverUrl;

    private String playUrl;

    private String videoTypeId;

    private String videoTypeTitle;

    private Integer dayPlayCount;

    private Boolean top;

    private String addTime;

    private Integer playCount;

    private Integer collectCount;

    private Integer duration;

    private String videoTags;

    private String userVideoCollectionTime;

    private String userPlayListTime;

    private Boolean isVip;

    private Boolean free;

    private String monthDay;

}