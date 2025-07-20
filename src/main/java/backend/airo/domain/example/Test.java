package backend.airo.domain.example;

import lombok.Getter;

@Getter
public class Test {

    private Long id = 0L;
    private String test;


    public Test(String test) {
        this.test = test;
    }

    public Test(Long id, String test) {
        this.id = id;
        this.test = test;
    }
}
