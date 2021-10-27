package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
public class User implements Serializable {

    @Id
    private String cc;
    @NotNull
    private String name;
    @NotNull
    private Date birthDate;
    @Email
    private String email;
    private int phoneNumber;

    public User() {
    }

    public User(String cc, String name, Date birthDate, String email, int phoneNumber) {
        this.cc = cc;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
