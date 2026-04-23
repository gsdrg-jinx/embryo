package com.example.a;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * AI接口控制器
 * 提供调用外部AI服务的REST API端点
 */
@CrossOrigin(origins = "*")
@RestController
public class A {

    /**
     * HTTP客户端实例，用于发起HTTP请求
     * RestTemplate是Spring提供的同步HTTP客户端工具类
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 调用外部AI服务生成文本
     *
     * @return 包含成功状态和数据的Map对象
     *         - 成功时: {"success": true, "data": "生成的文本内容"}
     *         - 失败时: {"success": false, "message": "错误信息"}
     */
    @GetMapping("/ai")
    public Map<String, Object> googleai(@RequestParam String query) {
        // 外部AI服务的API地址
        System.out.println("query:"+query);
        String url = "http://localhost:8000/ai/generate?query=" + query;

        try {
            // 向外部AI服务发送GET请求，获取响应
            // ResponseEntity包含HTTP响应的状态码、头信息和主体内容
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // 提取响应体中的JSON数据，转换为Map结构
            Map<String, Object> body = response.getBody();

            // 构建返回给前端的结果对象
            Map<String, Object> result = new HashMap<>();

            // 检查响应体是否有效且包含"text"字段
            if (body != null && body.containsKey("text")) {
                // 请求成功，提取文本数据
                result.put("success", true);
                result.put("data", body.get("text"));
            } else {
                // 响应数据格式不正确或缺少必要字段
                result.put("success", false);
                result.put("message", "未获取到有效的文本数据");
            }

            return result;
        } catch (Exception e) {
            // 捕获网络异常、超时等错误情况
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "调用AI接口失败: " + e.getMessage());
            return errorResult;
        }
    }
}
