package backend.airo.api.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {
    private String name;
    private String nickname;
    private String phoneNumber;
    private LocalDate birthDate;


}
