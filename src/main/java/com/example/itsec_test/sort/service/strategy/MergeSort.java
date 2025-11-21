package com.example.itsec_test.sort.service.strategy;

import java.util.ArrayList;
import java.util.List;

public class MergeSort implements SortStrategy {
    @Override
    public List<Integer> sort(List<Integer> numbers) {
        if (numbers.size() <= 1) {
            return numbers;
        }

        int middle = numbers.size() / 2;
        List<Integer> left = new ArrayList<>(numbers.subList(0, middle));
        List<Integer> right = new ArrayList<>(numbers.subList(middle, numbers.size()));

        List<Integer> sortedLeft = sort(left);
        List<Integer> sortedRight = sort(right);

        List<Integer> sorted = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < sortedLeft.size() && rightIndex < sortedRight.size()) {
            int leftNumber = sortedLeft.get(leftIndex);
            int rightNumber = sortedRight.get(rightIndex);

            if (leftNumber <= rightNumber) {
                sorted.add(leftNumber);
                leftIndex++;
            } else {
                sorted.add(rightNumber);
                rightIndex++;
            }
        }

        while (leftIndex < sortedLeft.size()) {
            sorted.add(sortedLeft.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < sortedRight.size()) {
            sorted.add(sortedRight.get(rightIndex));
            rightIndex++;
        }

        return sorted;
    }
}
