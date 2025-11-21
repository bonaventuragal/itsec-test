package com.example.itsec_test.sort.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.itsec_test.sort.constant.SortType;
import com.example.itsec_test.sort.dto.SortRequest;
import com.example.itsec_test.sort.service.strategy.BubbleSort;
import com.example.itsec_test.sort.service.strategy.MergeSort;
import com.example.itsec_test.sort.service.strategy.SelectionSort;
import com.example.itsec_test.sort.service.strategy.SortStrategy;

@Service
public class SortService {
    private static final Map<SortType, SortStrategy> STRATEGY_MAP = new HashMap<>();

    public SortService() {
        STRATEGY_MAP.put(SortType.BUBBLE, new BubbleSort());
        STRATEGY_MAP.put(SortType.MERGE, new MergeSort());
        STRATEGY_MAP.put(SortType.SELECTION, new SelectionSort());
    }

    public List<Integer> sort(SortRequest request) {
        List<Integer> numbers = request.getNumbers();
        SortType sortType = request.getSortType();
        SortStrategy sortStrategy = STRATEGY_MAP.get(sortType);

        List<Integer> result = sortStrategy.sort(numbers);
        
        return result;
    }
}
