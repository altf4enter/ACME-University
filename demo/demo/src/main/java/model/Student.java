package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Student extends  Person {
    private @Id
    @GeneratedValue Long studentId;

    private List<String> lecturerIds;
}
