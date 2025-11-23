package com.example.itsec_test.common.dto;

import lombok.Data;
import java.util.List;

@Data
public class PaginationResponse<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> items;
}
