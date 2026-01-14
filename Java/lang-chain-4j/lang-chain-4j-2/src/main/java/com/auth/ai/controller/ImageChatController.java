package com.auth.ai.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.NoApiKeyException;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@Tag(name = "图片聊天服务")
@RequestMapping("api/ai/image")
@RestController
public class ImageChatController {

    private final OpenAiChatModel openAiChatModelQWenVLFlash;

    private final WanxImageModel wanxImageModel;

    @Value("classpath:static/image-test-1.jpg")
    private Resource resource;

    public ImageChatController(@Qualifier("qwen3-vl-flash") OpenAiChatModel openAiChatModelQWenVLFlash, WanxImageModel wanxImageModel) {
        this.openAiChatModelQWenVLFlash = openAiChatModelQWenVLFlash;
        this.wanxImageModel = wanxImageModel;
    }

    @Operation(summary = "图片聊天Api演示")
    @GetMapping("chat")
    public String chat() throws IOException {
        byte[] byteArray = resource.getContentAsByteArray();
        String base64Data = Base64.getEncoder().encodeToString(byteArray);

        UserMessage userMessage = UserMessage.from(
                TextContent.from("图片中是谁？来自哪里？角色是什么？"),
                ImageContent.from(base64Data, "image/jpg")
        );

        ChatResponse chatResponse = openAiChatModelQWenVLFlash.chat(userMessage);
        return chatResponse.aiMessage().text();
    }

    @Operation(summary = "WanX图片生成Api演示")
    @GetMapping("image/wan-create")
    public String createWanXImage(@RequestParam(value = "message", defaultValue = "不要生成") String message) {
        Response<Image> imageResponse = wanxImageModel.generate(message);
        return imageResponse.content().url().toASCIIString();
    }

    @Operation(summary = "WanX图片生成Api演示")
    @GetMapping("image/wan-create-advance")
    public String createWanXImageAdvance() {
        String prompt = "近景镜头，18岁的中国女孩，古代服饰，圆脸，正面看着镜头，" +
                "民族优雅的服装，商业摄影，室外，电影级光照，半身特写，精致的淡妆，锐利的边缘。";

        ImageSynthesisParam imageSynthesisParam = ImageSynthesisParam.builder()
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .model(ImageSynthesis.Models.WANX_V1)
                .prompt(prompt)
                .style("<watercolor>")
                .n(1)
                // ['1024*1024', '720*1280', '1280*720', '768*1152']
                .size("1280*720")
                .build();

        ImageSynthesis imageSynthesis = new ImageSynthesis();
        try {
            ImageSynthesisResult imageSynthesisResult = imageSynthesis.call(imageSynthesisParam);
            return JSONUtil.toJsonStr(imageSynthesisResult);
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        }
    }

}

