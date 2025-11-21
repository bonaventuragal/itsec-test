package com.example.itsec_test.sort.dto;

import java.util.List;

import com.example.itsec_test.sort.constant.SortType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SortRequest {
    @NotNull
    private SortType sortType;

    @NotEmpty
    private List<Integer> numbers;
}
