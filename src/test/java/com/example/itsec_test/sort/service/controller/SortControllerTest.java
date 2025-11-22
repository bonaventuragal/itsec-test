package com.example.itsec_test.sort.service.controller;

import com.example.itsec_test.sort.constant.SortType;
import com.example.itsec_test.sort.controller.SortController;
import com.example.itsec_test.sort.dto.SortRequest;
import com.example.itsec_test.sort.service.SortService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

@WebMvcTest(SortController.class)
class SortControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SortService sortService;

	@Test
	void testSort() throws Exception {
		SortRequest request = new SortRequest();
		request.setNumbers(Arrays.asList(5, 3, 2, 1, 4));
		request.setSortType(SortType.BUBBLE);

		when(sortService.sort(request)).thenReturn(Arrays.asList(1, 2, 3, 4, 5));

		String json = "{\"numbers\":[5,3,2,1,4],\"sortType\":\"BUBBLE\"}";

		mockMvc.perform(post("/api/v1/sort")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numbers[0]").value(1))
				.andExpect(jsonPath("$.numbers[1]").value(2))
				.andExpect(jsonPath("$.numbers[2]").value(3))
				.andExpect(jsonPath("$.numbers[3]").value(4))
				.andExpect(jsonPath("$.numbers[4]").value(5));
	}
}
