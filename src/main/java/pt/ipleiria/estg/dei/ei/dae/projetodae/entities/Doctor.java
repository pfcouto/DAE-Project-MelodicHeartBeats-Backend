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
        ),
        @NamedQuery(
                name="getPrescriptionsOfDoctor",
                query="SELECT p FROM Prescription p WHERE p.doctor.username = :username"
        )
})
public class Doctor extends User implements Serializable {
    private String office;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private LinkedList<Prescription> prescriptions;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private LinkedList<Observation> observations;

    public Doctor() {
        super();
        prescriptions = new LinkedList<>();
        observations=new LinkedList<>();
    }

    public Doctor(String username, String password, String name, String birthDate, String email, String phoneNumber, String office) {
        super(username, password, name, birthDate, email, phoneNumber);
        this.office = office;
        prescriptions = new LinkedList<>();
        observations=new LinkedList<>();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }


    public LinkedList<Observation> getObservations() {
        return observations;
    }

    public void setObservations(LinkedList<Observation> observations) {
        this.observations = observations;
    }

    public void addObservation(Observation observation) {
        if (observation != null && !observations.contains(observation))
            observations.add(observation);
    }

    public void removeObservation(Observation observation) {
        if (observation != null && observations.contains(observation))
            observations.remove(observation);
    }


    public void addPrescription(Prescription prescription) {
        if (prescription != null && !prescriptions.contains(prescription))
            prescriptions.add(prescription);
    }

    public void removePrescription(Prescription prescription) {
        if (prescription != null && prescriptions.contains(prescription))
            prescriptions.remove(prescription);
    }

    public LinkedList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(LinkedList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
