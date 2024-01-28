package fr.univrouen.onlyfems.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {

    /**
     * Allow to get an object Pageable, used for the pagination.
     *
     * @param page Page number.
     * @param size Size of the page.
     * @return The Pageable object.
     */
    public static Pageable getPagination(int page, int size) {
        if (page == 0) {
            size = Integer.MAX_VALUE;
        } else {
            page--;
        }
        return PageRequest.of(page, size);
    }
}
