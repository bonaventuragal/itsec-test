package com.example.itsec_test.sort.service;

import com.example.itsec_test.sort.constant.SortType;
import com.example.itsec_test.sort.dto.SortRequest;
import com.example.itsec_test.sort.service.strategy.BubbleSort;
import com.example.itsec_test.sort.service.strategy.MergeSort;
import com.example.itsec_test.sort.service.strategy.SelectionSort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SortServiceTest {
	private BubbleSort bubbleSort;
	private MergeSort mergeSort;
	private SelectionSort selectionSort;
	private SortService sortService;

	@BeforeEach
	void setUp() {
		bubbleSort = mock(BubbleSort.class);
		mergeSort = mock(MergeSort.class);
		selectionSort = mock(SelectionSort.class);
		sortService = new SortService(bubbleSort, mergeSort, selectionSort);
	}

	@Test
	void testBubbleSort() {
		SortRequest request = new SortRequest();
		request.setNumbers(Arrays.asList(5, 3, 2, 1, 4));
		request.setSortType(SortType.BUBBLE);

		List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
		when(bubbleSort.sort(request.getNumbers())).thenReturn(expected);

		List<Integer> result = sortService.sort(request);
		assertEquals(expected, result);
		verify(bubbleSort, times(1)).sort(request.getNumbers());
		verifyNoInteractions(mergeSort);
		verifyNoInteractions(selectionSort);
	}

	@Test
	void testMergeSort() {
		SortRequest request = new SortRequest();
		request.setNumbers(Arrays.asList(5, 3, 2, 1, 4));
		request.setSortType(SortType.MERGE);

		List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
		when(mergeSort.sort(request.getNumbers())).thenReturn(expected);

		List<Integer> result = sortService.sort(request);
		assertEquals(expected, result);
		verify(mergeSort, times(1)).sort(request.getNumbers());
		verifyNoInteractions(bubbleSort);
		verifyNoInteractions(selectionSort);
	}

	@Test
	void testSelectionSort() {
		SortRequest request = new SortRequest();
		request.setNumbers(Arrays.asList(5, 3, 2, 1, 4));
		request.setSortType(SortType.SELECTION);

		List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
		when(selectionSort.sort(request.getNumbers())).thenReturn(expected);

		List<Integer> result = sortService.sort(request);
		assertEquals(expected, result);
		verify(selectionSort, times(1)).sort(request.getNumbers());
		verifyNoInteractions(bubbleSort);
		verifyNoInteractions(mergeSort);
	}
}
