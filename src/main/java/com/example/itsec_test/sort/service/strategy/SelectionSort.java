package com.example.itsec_test.sort.service.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SelectionSort implements SortStrategy {
    @Override
    public List<Integer> sort(List<Integer> numbers) {
        List<Integer> result = new ArrayList<Integer>(numbers);
        int n = result.size();
        for (int i = 0; i < n; i++) {
            int minElement = result.get(i);
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (result.get(j) < result.get(minIndex)) {
                    minElement = result.get(j);
                    minIndex = j;
                }
            }

            int temp = result.get(i);
            result.set(i, minElement);
            result.set(minIndex, temp);
        }
        return result;
    }
}
