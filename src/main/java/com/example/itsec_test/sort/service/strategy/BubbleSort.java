package com.example.itsec_test.sort.service.strategy;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort implements SortStrategy {
    @Override
    public List<Integer> sort(List<Integer> numbers) {
        List<Integer> result = new ArrayList<Integer>(numbers);
        int n = result.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (result.get(j) > result.get(j + 1)) {
                    int temp = result.get(j);
                    result.set(j, result.get(j + 1));
                    result.set(j + 1, temp);
                }
            }
        }
        return result;
    }
}