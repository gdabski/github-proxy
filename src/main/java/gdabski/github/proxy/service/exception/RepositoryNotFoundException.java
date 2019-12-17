package gdabski.github.proxy.service.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents a failure to find a repository of given parameters.
 */
@ToString @Getter
public class RepositoryNotFoundException extends RuntimeException {

    private final String owner;
    private final String name;

    public RepositoryNotFoundException(String owner, String name) {
        super("Could not find repository data for specified parameters.");
        this.owner = owner;
        this.name = name;
    }
}
