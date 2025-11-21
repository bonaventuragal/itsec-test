package com.example.itsec_test.sort.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SortRequest {
    @NotEmpty
    private List<Integer> numbers;
}
