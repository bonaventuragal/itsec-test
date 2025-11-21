package com.example.itsec_test.sort.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.itsec_test.sort.dto.SortRequest;
import com.example.itsec_test.sort.dto.SortResponse;
import com.example.itsec_test.sort.service.SortService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/sort")
@Tag(name = "Sort")
public class SortController {
    private final SortService sortService;

    public SortController(SortService sortService) {
        this.sortService = sortService;
    }

    @PostMapping()
    public SortResponse sort(@RequestBody SortRequest request) {
        List<Integer> result = this.sortService.sort(request);

        return SortResponse.builder()
                .numbers(result)
                .build();
    }

}
