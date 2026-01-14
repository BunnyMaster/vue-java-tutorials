package cn.bunny.mongodb.download;

import cn.bunny.mongodb.dao.enity.Response;
import cn.bunny.mongodb.dao.enity.VideoEntity;
import cn.bunny.mongodb.utils.HttpRequestUtils;
import cn.bunny.mongodb.utils.SystemControlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class VideoDownload {
    // 当前下载页
    private final static Integer currentPage = 7;

    // 线程池个数
    private final static Integer threadPoolSize = 10;

    // 开始线程池
    private final static ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

    public static void main(String[] args) {
        try {
            // 记录程序开始时间
            Instant start = Instant.now();

            String url = "https://mjfh136.cyou/view/videoList/1637462276570050562/" + currentPage + "/80";
            HttpRequestUtils<Response<VideoEntity>> requestUtils = new HttpRequestUtils<>();
            Response<VideoEntity> responseData = requestUtils.requestGET(url);

            // 接收到返回信息
            downloadVideoList(responseData);

            // 执行完成后播放音乐
            executorService.shutdown();
            boolean awaitTermination = executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            if (awaitTermination) {
                SystemControlUtils.playMusic();

                // 计算程序运行时间
                Instant end = Instant.now();
                Duration duration = Duration.between(start, end);
                long minutes = duration.toMinutes();
                long seconds = duration.minusMinutes(minutes).getSeconds();

                log.info("程序运行时间： {} 分钟 {} 秒", minutes, seconds);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * 下载视频列表内容
     *
     * @param responseData 返回一页的数据响应
     */
    private static void downloadVideoList(Response<VideoEntity> responseData) {
        // 使用原子化操作，记录每个下载当前索引
        AtomicInteger index = new AtomicInteger(0);

        // 拿到每个数据
        List<VideoEntity> videoEntities = responseData.getData().getList();
        LinkedBlockingQueue<VideoEntity> videoQueue = new LinkedBlockingQueue<>(videoEntities);
        videoQueue.stream()
                .filter(videoEntity -> videoEntity.getPlayUrl().contains("http"))
                .peek(videoEntity -> {
                    String videoTag = StringUtils.hasText(videoEntity.getVideoTag()) ? videoEntity.getVideoTag() : videoEntity.getVideoTypeTitle();
                    videoEntity.setVideoTag(videoTag);
                })
                .forEach((videoEntity) -> executorService.submit(() -> {
                    int currentIndex = index.incrementAndGet();
                    log.info("开始下载第：{}个，线程Id：{}", currentIndex, Thread.currentThread().getName());

                    // 整理命令行参数
                    List<String> command = new ArrayList<>();
                    command.add("N_m3u8DL-CLI");
                    command.add("\"" + videoEntity.getPlayUrl() + "\"");
                    command.add("--workDir");
                    command.add("\"G:\\video\\" + videoEntity.getVideoTag() + "\"");
                    command.add("--saveName");
                    command.add("\"" + videoEntity.getTitle().trim() + "\"");
                    command.add("--enableDelAfterDone");
                    command.add("--enableBinaryMerge");
                    command.add("--enableMuxFastStart");

                    // 开始下载
                    SystemControlUtils.startProcess(command, currentIndex);
                }));
    }
}
