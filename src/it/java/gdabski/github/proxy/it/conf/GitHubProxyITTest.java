package gdabski.github.proxy.it.conf;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import java.lang.annotation.*;

import gdabski.github.proxy.GitHubProxyApplication;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * A <em>composed annotation</em> that enables the Spring Boot test framework, specifies
 * the Spring context including the application and additional testing facilities and
 * enables the <i>it</i> Spring profile.
 *
 * @see ActiveProfiles
 * @see SpringJUnitConfig
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ActiveProfiles("it")
@ContextConfiguration(
        classes = {GitHubProxyApplication.class, GitHubProxyITConfiguration.class},
        initializers = {ConfigFileApplicationContextInitializer.class, DynamicPropertyContextInitializer.class}
)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public @interface GitHubProxyITTest {
}
