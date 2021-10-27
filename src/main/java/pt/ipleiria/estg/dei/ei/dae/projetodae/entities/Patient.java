package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;

@Entity
public class Patient extends User implements Serializable {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private LinkedHashSet<Prescription> prescriptions;

    public Patient() {
        prescriptions = new LinkedHashSet<>();
    }

    public Patient(String cc, String name, Date birthDate, String email, int phoneNumber) {
        super(cc, name, birthDate, email, phoneNumber);
    }
}
