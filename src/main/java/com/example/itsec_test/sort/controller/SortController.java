package com.example.itsec_test.sort.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.itsec_test.sort.dto.SortRequest;
import com.example.itsec_test.sort.dto.SortResponse;
import com.example.itsec_test.sort.service.BubbleSortService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/sort")
@Tag(name = "Sort")
public class SortController {
    private final BubbleSortService bubbleSortService;

    public SortController(BubbleSortService bubbleSortService) {
        this.bubbleSortService = bubbleSortService;
    }

    @PostMapping("bubble")
    public SortResponse bubbleSort(@RequestBody SortRequest request) {
        List<Integer> result = this.bubbleSortService.sort(request.getNumbers());

        return SortResponse.builder()
                .numbers(result)
                .build();
    }

}
