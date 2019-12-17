package gdabski.github.proxy.dto;

import java.time.Instant;

public class RepositoryDtoFixtures {

    public static RepositoryDto.RepositoryDtoBuilder repositoryDtoBuilder() {
        return RepositoryDto.builder()
                .fullName("github-proxy")
                .description("description of github-proxy")
                .cloneUrl("https://example.com/gdabski/github-proxy.git")
                .stars(1)
                .createdAt(Instant.parse("2018-01-03T23:14:32Z"));
    }

    public static RepositoryDto newRepositoryDto() {
        return repositoryDtoBuilder().build();
    }

}