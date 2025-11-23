package com.example.itsec_test.common.dto;

import lombok.Data;

@Data
public class PaginationRequest {
    private int page = 1;
    private int size = 10;
}
