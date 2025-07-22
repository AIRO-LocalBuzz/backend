package backend.airo.persistence.abstracts;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class ImmutableEntity {

    @CreatedDate
    private LocalDateTime createdAt;

}
