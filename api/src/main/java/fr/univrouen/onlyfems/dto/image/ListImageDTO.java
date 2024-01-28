package fr.univrouen.onlyfems.dto.image;

import fr.univrouen.onlyfems.entities.Image;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ListImageDTO {
    private final List<ImageDTO> images;
    private final int totalPages;
    private final long totalElements;

    public ListImageDTO(List<ImageDTO> images, int totalPages, long totalElements) {
        this.images = images;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public ListImageDTO(Page<Image> page) {
        this.images = page.getContent().stream().map(ImageDTO::new).toList();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
