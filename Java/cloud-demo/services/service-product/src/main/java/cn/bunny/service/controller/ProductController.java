package cn.bunny.service.controller;

import cn.bunny.model.product.bean.Product;
import cn.bunny.service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "根据id查询商品")
    @GetMapping("{id}")
    public Product getProduct(@PathVariable("id") Long productId, HttpServletRequest request) {
        // try {
        //     TimeUnit.SECONDS.sleep(6);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }

        System.out.println("request-X-Token: " + request.getHeader("X-Token"));
        return productService.getProductById(productId);
    }
}
