package gdabski.github.proxy.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import gdabski.github.proxy.dto.RepositoryDto;
import gdabski.github.proxy.dto.RepositoryDtoFixtures;
import gdabski.github.proxy.service.RepositoryService;
import org.junit.jupiter.api.Test;

class RepositoryControllerTest {

    private static final String OWNER = "owner";
    private static final String NAME = "name";

    private final RepositoryService repositoryService = mock(RepositoryService.class);
    private final RepositoryController controller = new RepositoryController(repositoryService);

    @Test
    public void shouldReturnRepositoryDtoFromService() {
        // given
        RepositoryDto repositoryDto = RepositoryDtoFixtures.newRepositoryDto();
        when(repositoryService.getRepository(OWNER, NAME)).thenReturn(repositoryDto);

        // when
        RepositoryDto returned = controller.getRepository(OWNER, NAME);

        // then
        verify(repositoryService).getRepository(OWNER, NAME);
        assertEquals(repositoryDto, returned);
    }

}