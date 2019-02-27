package dominos.demo.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogInDTO {

    private String email;
    private String password;

    public UserLogInDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
