package com.sumerge.task.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="CourseClient", url = "http://localhost:8081")
public interface CourseClient {
    @GetMapping("/coursesXML")
    String coursesXML();
}
