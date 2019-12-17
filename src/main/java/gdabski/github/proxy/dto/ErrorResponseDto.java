package gdabski.github.proxy.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ErrorResponseDto {
    private final String message;
}
