package cn.noload.nas.web.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tx")
public class TestResource {

    @PostMapping("/accept/test")
    public ResponseEntity<Map<String, String>> test(
        @RequestBody Map<String, String> body
    ) {
        System.out.println(body);
        return ResponseEntity.ok(body);
    }
}
