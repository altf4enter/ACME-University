package model;

import lombok.Data;

@Data
public class Person {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;

}
