package gdabski.github.proxy.service.exception;

/**
 * Represents any issue accessing repository data.
 */
public class RepositoryDataAccessException extends RuntimeException {

    public RepositoryDataAccessException(Throwable cause) {
            super("Failed to access repository data.", cause);
    }

}
