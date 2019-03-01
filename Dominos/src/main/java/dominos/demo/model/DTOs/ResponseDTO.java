package dominos.demo.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO {

    private String msg;
    private LocalDateTime date;

}
