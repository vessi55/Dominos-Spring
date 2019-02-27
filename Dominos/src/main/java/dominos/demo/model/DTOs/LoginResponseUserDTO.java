package dominos.demo.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Date date;

    public LoginResponseUserDTO(String firstName, String lastName, String email, Date date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.date = date;
    }
}
