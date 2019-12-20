package gdabski.github.proxy.dao;

import static com.google.common.collect.Iterables.getOnlyElement;
import static gdabski.github.proxy.dao.model.RepositoryFixtures.newRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;

import gdabski.github.proxy.dao.model.Repository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

class GitHubV3RestClientTest {

    private static final String ROOT_URI = "http://root.uri/path";

    private final RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class, RETURNS_SELF);
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final GitHubV3RestClient gitHubClient;

    private GitHubV3RestClientTest() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        gitHubClient = new GitHubV3RestClient(restTemplateBuilder, ROOT_URI);
    }


    @Test
    public void shouldBuildRestTemplate() {
        // then
        verify(restTemplateBuilder).rootUri(ROOT_URI);
        verify(restTemplateBuilder).setConnectTimeout(any());
        verify(restTemplateBuilder).setReadTimeout(any());
        verify(restTemplateBuilder).build();
        verifyNoMoreInteractions(restTemplateBuilder, restTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnFetchedRepository() {
        // given
        ResponseEntity<Repository> response = ResponseEntity.ok(newRepository());
        when(restTemplate.exchange(anyString(), any(), any(), eq(Repository.class))).thenReturn(response);

        // when
        Repository repository = gitHubClient.getRepository("owner", "name");

        // then
        ArgumentCaptor<HttpEntity<?>> requestEntity = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/repos/owner/name"), eq(GET), requestEntity.capture(), eq(Repository.class));

        String acceptHeader = getOnlyElement(requestEntity.getValue().getHeaders().getAccept()).toString();
        assertEquals("application/vnd.github.v3+json", acceptHeader);
        assertEquals(repository, response.getBody());
    }

    @Test
    public void shouldPropagateRestClientException() {
        // given
        Exception e = new RestClientException("exception occurred");
        when(restTemplate.exchange(anyString(), any(), any(), eq(Repository.class))).thenThrow(e);

        // when and then
        RestClientException thrown = assertThrows(RestClientException.class, () -> {
                gitHubClient.getRepository("owner", "name");
        });
        assertEquals(e, thrown);
    }

}