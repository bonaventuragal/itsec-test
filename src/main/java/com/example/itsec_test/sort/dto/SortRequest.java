package com.example.itsec_test.sort.dto;

import java.util.List;

import com.example.itsec_test.sort.constant.SortType;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SortRequest {
    @NotEmpty
    private SortType sortType;

    @NotEmpty
    private List<Integer> numbers;
}
