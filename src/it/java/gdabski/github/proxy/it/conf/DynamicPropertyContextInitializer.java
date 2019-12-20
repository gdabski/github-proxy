package gdabski.github.proxy.it.conf;

import static org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Overrides selected application properties with values necessary for self-contained
 * execution of IT test suite.
 * <p>
 * Dynamic properties are added to the Spring environment as if specified on test class
 * through {@link org.springframework.test.context.TestPropertySource @TestPropertySource}
 * annotation.
 */
@Slf4j
public class DynamicPropertyContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final int EPHEMERAL_MIN = 49152;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        int serverPort = findAvailableEphemeralTcpPort();
        log.info("Selected port {} for server.port property", serverPort);

        int wiremockPort = findAvailableEphemeralTcpPort();
        log.info("Selected port {} for wiremock.port property", wiremockPort);

        addInlinedPropertiesToEnvironment(applicationContext,
                property("server.port", serverPort),
                property("wiremock.port", wiremockPort),
                property("github.api.root", "http://localhost:" + wiremockPort)
        );
    }

    private static int findAvailableEphemeralTcpPort() {
        return findAvailableTcpPort(EPHEMERAL_MIN);
    }

    private static String property(String name, Object value) {
        return String.format("%s=%s", name, value);
    }
}
