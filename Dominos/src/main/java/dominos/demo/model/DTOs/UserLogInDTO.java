package dominos.demo.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogInDTO {

    private String username;
    private String password;

    public UserLogInDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
