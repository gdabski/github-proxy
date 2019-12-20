package gdabski.github.proxy.it.conf;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("it")
public class GitHubProxyITConfiguration {

    GitHubProxyITConfiguration() {}

    @Bean(destroyMethod = "stop")
    public WireMockServer wireMockServer(@Value("${wiremock.port}") int port) {
        WireMockConfiguration wireMockOptions = options().port(port).usingFilesUnderClasspath("wiremock");
        WireMockServer wireMockServer = new WireMockServer(wireMockOptions);
        wireMockServer.start();
        return wireMockServer;
    }
}
