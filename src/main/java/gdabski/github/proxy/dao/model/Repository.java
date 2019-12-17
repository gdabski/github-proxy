package gdabski.github.proxy.dao.model;

import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class Repository {

    private final String fullName;
    private final String description;
    private final String cloneUrl;
    private final int stargazersCount;
    private final Instant createdAt;

}
