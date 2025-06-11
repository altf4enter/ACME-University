package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Lecturer extends  Person {
    private @Id
    @GeneratedValue Long lecturerId;

    private List<String> studentIds;
}
