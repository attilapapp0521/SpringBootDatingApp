package com.datingapp.helpers;

public class PaginationHeader {

    private int currentPage;
    private int itemsPerPage;
    private long totalItems;
    private int totalPages;

    public PaginationHeader(int currentPage, int itemsPerPage,
                            long totalItems, int totalPages) {

        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
