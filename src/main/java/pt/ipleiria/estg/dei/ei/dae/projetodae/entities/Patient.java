package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.swing.*;
import java.io.Serializable;
import java.util.Date;
//import java.util.LinkedHashSet;
import java.util.LinkedList;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPatients",
                query = "SELECT p FROM Patient p ORDER BY p.name" // JPQL
        )
})
public class Patient extends User implements Serializable {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private LinkedList<Prescription> prescriptions;

    public Patient() {
        prescriptions = new LinkedList<>();
    }

    public Patient(String username, String password, String name, String birthDate, String email, String phoneNumber) {
        super(username, password, name, birthDate, email, phoneNumber);
        prescriptions = new LinkedList<>();
    }

    public LinkedList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(LinkedList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addPrescription(Prescription prescription) {
        if (prescription != null && !prescriptions.contains(prescription))
            prescriptions.add(prescription);
    }

    public void removePrescription(Prescription prescription) {
        if (prescription != null && prescriptions.contains(prescription))
            prescriptions.remove(prescription);
    }
}
