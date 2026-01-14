package cn.bunny.mongodb.utils;

import cn.bunny.mongodb.dao.enity.Response;
import cn.bunny.mongodb.dao.enity.VideoEntity;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
public class HttpRequestUtils<T> {

    /**
     * 使用GET请求
     *
     * @param url 请求地址
     * @return 返回泛型 T
     */
    public T requestGET(String url) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                    .GET()
                    .timeout(Duration.of(20, ChronoUnit.SECONDS))
                    .build();

            // 开始下载
            log.info("开始请求：{}", url);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            // 使用 TypeReference 来解析泛型类型
            TypeReference<Response<VideoEntity>> typeReference = new TypeReference<>() {
            };
            return JSON.parseObject(body, typeReference.getType());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
