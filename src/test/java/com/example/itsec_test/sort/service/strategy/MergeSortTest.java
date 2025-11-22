package com.example.itsec_test.sort.service.strategy;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {
	@Test
	void testSort() {
		MergeSort mergeSort = new MergeSort();
		List<Integer> input = Arrays.asList(5, 3, 2, 1, 4);
		List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> result = mergeSort.sort(input);
		assertEquals(expected, result);
	}
}
