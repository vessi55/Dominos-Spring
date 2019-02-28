package dominos.demo.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDTO {

    private String newFirstName;
    private String newLastName;
    private String newEmail;
    private String currentPassword;
    private String newPassword;
    private String repeatPassword;
}
