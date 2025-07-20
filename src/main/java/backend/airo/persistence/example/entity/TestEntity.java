package backend.airo.persistence.example.entity;

import backend.airo.domain.example.Test;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private String test;

    public TestEntity(String test) {
        this.test = test;
    }

    public static TestEntity toEntity(Test test) {
        return new TestEntity(test.getTest());
    }

    public static Test toDomain(TestEntity test) {
        return new Test(test.getId(),test.getTest());
    }
}
