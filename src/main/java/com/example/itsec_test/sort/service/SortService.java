package com.example.itsec_test.sort.service;

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
    private final Map<SortType, SortStrategy> strategyMap = new HashMap<>();

    public SortService(BubbleSort bubbleSort, MergeSort mergeSort, SelectionSort selectionSort) {
        strategyMap.put(SortType.BUBBLE, bubbleSort);
        strategyMap.put(SortType.MERGE, mergeSort);
        strategyMap.put(SortType.SELECTION, selectionSort);
    }

    public List<Integer> sort(SortRequest request) {
        List<Integer> numbers = request.getNumbers();
        SortType sortType = request.getSortType();
        SortStrategy sortStrategy = strategyMap.get(sortType);
        return sortStrategy.sort(numbers);
    }
}
