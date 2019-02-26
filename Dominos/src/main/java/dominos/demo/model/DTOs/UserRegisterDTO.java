package dominos.demo.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTO {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String password2;

    public UserRegisterDTO(String first_name, String last_name, String email, String password, String password2) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }
}
