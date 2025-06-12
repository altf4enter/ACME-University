package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@NoArgsConstructor
public class Person {

    public Person(String name,String surname){
        this.name = name;
        this.surname = surname;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message= "Name must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name must be alphanumeric")
    private String name;

    @NotBlank(message= "Surname must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Surname must be alphanumeric")
    private String surname;


}
