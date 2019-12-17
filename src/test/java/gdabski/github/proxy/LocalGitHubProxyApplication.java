package gdabski.github.proxy;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class LocalGitHubProxyApplication {

    private LocalGitHubProxyApplication() {}

    public static void main(String[] args) {
        new SpringApplicationBuilder(GitHubProxyApplication.class)
                .profiles("dev")
                .run(args);
    }

}
