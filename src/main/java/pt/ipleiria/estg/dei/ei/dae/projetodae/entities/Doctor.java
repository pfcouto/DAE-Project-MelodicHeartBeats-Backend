package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;

@Entity
public class Doctor extends User implements Serializable {
    @NotNull
    private int code;
    private String office;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private LinkedHashSet<Prescription> prescriptions;

    public Doctor() {
        prescriptions = new LinkedHashSet<>();
    }

    public Doctor(String cc, String name, Date birthDate, String email, int phoneNumber, int code, String office) {
        super(cc, name, birthDate, email, phoneNumber);
        this.code = code;
        this.office = office;
        prescriptions = new LinkedHashSet<>();
    }
}
