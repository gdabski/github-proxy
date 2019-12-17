package gdabski.github.proxy.dao.model;

import java.time.Instant;

public class RepositoryFixtures {

    public static Repository.RepositoryBuilder repositoryBuilder() {
        return Repository.builder()
                .fullName("github-proxy")
                .description("description of github-proxy")
                .cloneUrl("https://example.com/gdabski/github-proxy.git")
                .stargazersCount(1)
                .createdAt(Instant.parse("2018-01-03T23:14:32Z"));
    }

    public static Repository newRepository() {
        return repositoryBuilder().build();
    }

}