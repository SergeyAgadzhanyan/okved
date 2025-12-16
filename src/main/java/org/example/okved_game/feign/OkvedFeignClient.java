package org.example.okved_game.feign;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "okvedClient",
        url = "https://raw.githubusercontent.com"
)
public interface OkvedFeignClient {

    @GetMapping("/bergstar/testcase/master/okved.json")
    String getOkved();
}