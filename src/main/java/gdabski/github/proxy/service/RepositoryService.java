package gdabski.github.proxy.service;

import static lombok.AccessLevel.PACKAGE;

import gdabski.github.proxy.dao.GitHubV3RestClient;
import gdabski.github.proxy.dao.model.Repository;
import gdabski.github.proxy.dto.RepositoryDto;
import gdabski.github.proxy.service.exception.RepositoryDataAccessException;
import gdabski.github.proxy.service.exception.RepositoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

/**
 * Performs business operations on {@link Repository}s.
 */
@Slf4j
@Service
@RequiredArgsConstructor(access = PACKAGE)
public class RepositoryService {

    private final GitHubV3RestClient gitHubClient;

    /**
     * Returns details of a GitHub repository.
     * @param owner the repository's owner
     * @param name the repository's name
     * @return {@link RepositoryDto} for the provided owner and name
     * @throws RepositoryNotFoundException when no repository for given owner
     * and name can be found
     * @throws RepositoryDataAccessException when any issue prohibits successful
     * access to repository data
     */
    public RepositoryDto getRepository(String owner, String name) {
        try {
            Repository repository = gitHubClient.getRepository(owner, name);
            return buildRepositoryDto(repository);
        } catch (HttpStatusCodeException e) {
            throw exceptionForStatusCode(e, owner, name);
        } catch (RestClientException e) {
            throw genericDataAccessException(e);
        }
    }

    private static RepositoryDto buildRepositoryDto(Repository repository) {
        return RepositoryDto.builder()
                .fullName(repository.getFullName())
                .description(repository.getDescription())
                .cloneUrl(repository.getCloneUrl())
                .stars(repository.getStargazersCount())
                .createdAt(repository.getCreatedAt())
                .build();
    }

    private static RuntimeException exceptionForStatusCode(HttpStatusCodeException exception, String owner, String name) {
        if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.info("Failed to find repository data for owner {} and name {}", owner, name);
            return new RepositoryNotFoundException(owner, name);
        } else {
            log.error("Received response code {} when attempting to find repository data",
                    exception.getRawStatusCode());
            return new RepositoryDataAccessException(exception);
        }
    }

    private static RuntimeException genericDataAccessException(RestClientException exception) {
        log.error("Unexpected exception when attempting to access repository data", exception);
        return new RepositoryDataAccessException(exception);
    }

}
