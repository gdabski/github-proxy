package gdabski.github.proxy.it;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import gdabski.github.proxy.it.conf.GitHubProxyITTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@GitHubProxyITTest
public class RepositoryIT {

    private static final String GITHUB_V3_MEDIA_TYPE = "application/vnd.github.v3+json";

    @Inject
    private WireMockServer gitHubMock;

    @Inject @Value("${server.port}")
    private int gitHubProxyPort;

    @Test
    public void shouldReturnRepository_when_presentOnGitHub() {
        // given
        gitHubMock.stubFor(get("/repos/gdabski/test")
                .willReturn(ok().withBodyFile("repository/ok-repository.json")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );

        // when
        Response response = given().port(gitHubProxyPort)
                .when().get("/repositories/gdabski/test");

        // then
        gitHubMock.verify(getRequestedFor(urlEqualTo("/repos/gdabski/test"))
        .withHeader(ACCEPT, WireMock.equalTo(GITHUB_V3_MEDIA_TYPE)));

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_JSON_VALUE)
                .body("fullName", equalTo("gdabski/test"))
                .body("description", equalTo("A basic Angular app created during training."))
                .body("cloneUrl", equalTo("https://github.com/gdabski/test.git"))
                .body("stars", equalTo(12))
                .body("createdAt", equalTo("2016-10-18T07:06:47Z"));
    }

    @Test
    public void shouldReturnRepositoryNotFound_when_notPresentOnGitHub() {
        // given
        gitHubMock.stubFor(get("/repos/gdabski/test")
                .willReturn(notFound().withBodyFile("repository/notfound-repository.json")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );

        // when
        Response response = given().port(gitHubProxyPort)
                .when().get("/repositories/gdabski/test");

        // then
        gitHubMock.verify(getRequestedFor(urlEqualTo("/repos/gdabski/test"))
                .withHeader(ACCEPT, WireMock.equalTo(GITHUB_V3_MEDIA_TYPE)));

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(APPLICATION_JSON_VALUE)
                .body("message", not(emptyString()));
    }

    @AfterEach
    public void tearDown() {
        gitHubMock.resetAll();
    }

}
