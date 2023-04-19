package com.datingapp.extension;

import org.springframework.http.HttpHeaders;

public class HttpExtension {

	public static void addPaginationHeader(HttpHeaders httpHeaders, int currentPage,
			int itemsPerPage, long totalItems,
			int totalPages) {

		httpHeaders.add("Access-Control-Expose-Hearers", "Pagination");
	}
}
