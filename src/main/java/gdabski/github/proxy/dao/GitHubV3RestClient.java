package gdabski.github.proxy.dao;

import java.time.Duration;

import gdabski.github.proxy.dao.model.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

/**
 * Accesses GitHub resources using the <a href="https://developer.github.com/v3/">GitHub V3 REST API</a>.
 */
@Slf4j
@Component
public class GitHubV3RestClient {

    private static final UriTemplate REPOSITORY_ENDPOINT = new UriTemplate("/repos/{owner}/{fullName}");

    private final RestTemplate restTemplate;

    GitHubV3RestClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${github.api.root}") String rootUri) {
        this.restTemplate = restTemplateBuilder
                .rootUri(rootUri)
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
       log.info("Initialized GitHubV3RestClient with URL {}", rootUri);
    }

    /**
     * Returns details of a GitHub repository.
     * @param owner the repository's owner
     * @param name the repository's name
     * @return {@link Repository} for the provided owner and name
     * @throws org.springframework.web.client.RestClientException whenever the request
     * fails with an HTTP error code or an I/O issue.
     */
    public Repository getRepository(String owner, String name) {
        String url = REPOSITORY_ENDPOINT.expand(owner, name).toString();
        log.info("Requesting repository data from GitHub with URL {}", url);
        ResponseEntity<Repository> response = restTemplate.getForEntity(url, Repository.class);
        log.info("Received repository data from GitHub with response status {}", response.getStatusCodeValue());
        return response.getBody();
    }

}
