package cn.bunny.service.controller;

import cn.bunny.pojo.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

@Tag(name = "webflux测试i请求接口")
@RequestMapping("/api")
@RestController
public class WebFluxController {

    @Operation(summary = "Mono测试")
    @GetMapping("test1")
    public Mono<Result<String>> test1() {
        Result<String> result = Result.success("测试成功啦，第一个Mono");
        return Mono.just(result);
    }

    @Operation(summary = "Flux测试")
    @GetMapping("test2")
    public Flux<Result<ArrayList<String>>> test2() {
        ArrayList<String> list = new ArrayList<>() {
            {
                add("Flux测试，返回的是数组");
                add("数组啊啊啊啊");
            }
        };
        Result<ArrayList<String>> result = Result.success(list);

        return Flux.just(result);
    }


    @Operation(summary = "模仿ChatGPT")
    @GetMapping("test3")
    public Flux<String> test3() {
        ArrayList<String> list = new ArrayList<>() {
            {
                add("Flux测试，返回的是数组");
                add("数组啊啊啊啊");
            }
        };
        Result<ArrayList<String>> result = Result.success(list);

        return Flux.range(1, 10).map(i -> "输出：" + i).delayElements(Duration.ofMillis(500));
    }
}
