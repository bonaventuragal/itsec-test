package com.example.itsec_test.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
