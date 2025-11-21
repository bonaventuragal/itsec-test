package com.example.itsec_test.sort.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SortResponse {
    private List<Integer> numbers;
}
