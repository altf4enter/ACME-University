package model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Person {

    @NotBlank(message= "Name must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name must be alphanumeric")
    private String name;

    @NotBlank(message= "Surname must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Surname must be alphanumeric")
    private String surname;

}
