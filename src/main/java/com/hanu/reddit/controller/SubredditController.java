package com.hanu.reddit.controller;

import com.hanu.reddit.dto.SubredditDto;
import com.hanu.reddit.services.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDTO));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddit() {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAllSubreddit());
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "HelloWorld";
    }
}
