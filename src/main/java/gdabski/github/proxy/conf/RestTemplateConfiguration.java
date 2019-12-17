package gdabski.github.proxy.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

@Configuration
class RestTemplateConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public RestTemplateRequestCustomizer<ClientHttpRequest> userAgentRequestCustomizer() {
        String userAgent = String.format("%s (Java/%s)", appName, System.getProperty("java.version"));
        return request -> request.getHeaders().set(HttpHeaders.USER_AGENT, userAgent);
    }
}
