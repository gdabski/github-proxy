package gdabski.github.proxy.control;

import static lombok.AccessLevel.PACKAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import gdabski.github.proxy.dto.ErrorResponseDto;
import gdabski.github.proxy.dto.RepositoryDto;
import gdabski.github.proxy.service.RepositoryService;
import gdabski.github.proxy.service.exception.RepositoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "repositories", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(access = PACKAGE)
public class RepositoryController {

    private final RepositoryService repositoryService;

    @GetMapping("/{owner}/{name}")
    public RepositoryDto getRepository(@PathVariable String owner, @PathVariable String name) {
        log.info("Received request to get repository data for {}/{}", owner, name);
        RepositoryDto repository = repositoryService.getRepository(owner, name);
        log.info("Returning repository data for {}", repository.getFullName());
        return repository;
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(RepositoryNotFoundException.class)
    public ErrorResponseDto handleNotFoundException(RepositoryNotFoundException e) {
        return ErrorResponseDto.builder()
                .message(String.format("Repository %s/%s not found.", e.getOwner(), e.getName()))
                .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponseDto handleDataAccessException() {
        return ErrorResponseDto.builder()
                .message("Unexpected error. Contact system administrator if problem persists.")
                .build();
    }

}
