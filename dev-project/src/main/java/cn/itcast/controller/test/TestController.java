package cn.itcast.controller.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lisong
 * @Since: 2023/1/30 16:18
 */

@RestController
@RequestMapping
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @GetMapping("/test")
    public String getTest(){
        logger.warn("测试");
        return "success";
    }
}
