package gdabski.github.proxy.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class RepositoryDto {

    private final String fullName;
    private final String description;
    private final String cloneUrl;
    private final int stars;
    private final Instant createdAt;

}
