package com.amigoscode.librarymanagement.entity;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
@Entity
@Table (name="members")

public class Member {
    @Id
    @SequenceGenerator(
            name = "member_id_sequence",
            sequenceName = "member_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_id_sequence"
    )
    private Integer id;
    @NotBlank(message = "Name is required")
    private  String  name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private  String  email;
    public  Member(){

    }
    public Member(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
