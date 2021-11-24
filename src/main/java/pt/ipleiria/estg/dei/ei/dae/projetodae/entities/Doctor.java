package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllDoctors",
                query = "SELECT d FROM Doctor d ORDER BY d.name" // JPQL
        )
})
public class Doctor extends User implements Serializable {
    private String office;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private LinkedList<Prescription> prescriptions;

    public Doctor() {
        prescriptions = new LinkedList<>();
    }

    public Doctor(String username, String password, String name, Date birthDate, String email, int phoneNumber, String office) {
        super(username, password, name, birthDate, email, phoneNumber);
        this.office = office;
        prescriptions = new LinkedList<>();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public LinkedList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(LinkedList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
