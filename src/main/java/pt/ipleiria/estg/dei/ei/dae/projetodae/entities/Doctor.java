package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;

@Entity
public class Doctor extends User implements Serializable {
    private String office;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private LinkedList<Prescription> prescriptions;

    public Doctor() {
        prescriptions = new LinkedList<>();
    }

    public Doctor(String username, String password, String name, Date birthDate, String email, int phoneNumber, int code, String office) {
        super(username, password, name, birthDate, email, phoneNumber);
        this.office = office;
        prescriptions = new LinkedList<>();
    }
}
