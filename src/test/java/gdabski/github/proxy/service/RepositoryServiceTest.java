package gdabski.github.proxy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import gdabski.github.proxy.dao.GitHubV3RestClient;
import gdabski.github.proxy.dao.model.Repository;
import gdabski.github.proxy.dao.model.RepositoryFixtures;
import gdabski.github.proxy.dto.RepositoryDto;
import gdabski.github.proxy.service.exception.RepositoryDataAccessException;
import gdabski.github.proxy.service.exception.RepositoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

class RepositoryServiceTest {

    private static final String OWNER = "owner";
    private static final String NAME = "name";

    private final GitHubV3RestClient gitHubClient = mock(GitHubV3RestClient.class);
    private final RepositoryService repositoryService = new RepositoryService(gitHubClient);

    @Test
    public void shouldReturnRepositoryDto_when_gitHubClientReturns() {
        // given
        Repository repository = RepositoryFixtures.newRepository();
        when(gitHubClient.getRepository(any(), any())).thenReturn(repository);

        // when
        RepositoryDto dto = repositoryService.getRepository(OWNER, NAME);

        // then
        verify(gitHubClient).getRepository(OWNER, NAME);

        assertEquals(repository.getFullName(), dto.getFullName());
        assertEquals(repository.getDescription(), dto.getDescription());
        assertEquals(repository.getCloneUrl(), dto.getCloneUrl());
        assertEquals(repository.getStargazersCount(), dto.getStars());
        assertEquals(repository.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    public void shouldThrowNotFoundException_when_statusCode404() {
        // given
        when(gitHubClient.getRepository(any(), any())).thenThrow(new HttpClientErrorException(NOT_FOUND));

        // when and then
        RepositoryNotFoundException thrown = assertThrows(RepositoryNotFoundException.class, () -> {
            repositoryService.getRepository(OWNER, NAME);
        });
        assertEquals(OWNER, thrown.getOwner());
        assertEquals(NAME, thrown.getName());
    }

    @Test
    public void shouldThrowDataAccessException_when_statusCodeNot404() {
        // given
        when(gitHubClient.getRepository(any(), any())).thenThrow(new HttpServerErrorException(INTERNAL_SERVER_ERROR));

        // when and then
        assertThrows(RepositoryDataAccessException.class, () -> {
            repositoryService.getRepository(OWNER, NAME);
        });
    }

    @Test
    public void shouldThrowDataAccessException_when_otherRestClientException() {
        // given
        RestClientException exception = new RestClientException("error");
        when(gitHubClient.getRepository(any(), any())).thenThrow(exception);

        // when and then
        RepositoryDataAccessException thrown = assertThrows(RepositoryDataAccessException.class, () -> {
            repositoryService.getRepository(OWNER, NAME);
        });
        assertEquals(exception, thrown.getCause());
    }

}