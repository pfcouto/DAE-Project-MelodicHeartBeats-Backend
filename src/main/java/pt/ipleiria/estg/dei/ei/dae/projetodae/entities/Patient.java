package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPatients",
                query = "SELECT p FROM Patient p ORDER BY p.name" // JPQL
        ),

        @NamedQuery(
                name = "getAllPatientsNotDeleted",
                query = "SELECT p FROM Patient p WHERE p.blocked <> true ORDER BY p.name" // JPQL
        ),
        @NamedQuery(
                name = "getPrescriptionsOfPatient",
                query = "SELECT p FROM Prescription p WHERE p.patient.username = :username"
        )
})


public class Patient extends User implements Serializable {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private LinkedList<Prescription> prescriptions;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.REMOVE)
    private LinkedList<Observation> observations;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private LinkedList<PRC> prcs;

    public Patient() {
        observations=new LinkedList<>();
        prescriptions = new LinkedList<>();
        prcs = new LinkedList<>();
    }

    public Patient(String username, String password, String name, String birthDate, String email, String phoneNumber) {
        super(username, password, name, birthDate, email, phoneNumber);
        observations=new LinkedList<>();
        prescriptions = new LinkedList<>();
        prcs = new LinkedList<>();
    }

    public LinkedList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(LinkedList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public LinkedList<PRC> getPrcs() {
        return prcs;
    }

    public void setPrcs(LinkedList<PRC> prcs) {
        this.prcs = prcs;
    }

    public void addPrescription(Prescription prescription) {
        if (prescription != null && !prescriptions.contains(prescription))
            prescriptions.add(prescription);
    }

    public void removePrescription(Prescription prescription) {
        if (prescription != null && prescriptions.contains(prescription))
            prescriptions.remove(prescription);
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


    public void addPRC(PRC prc) {
        if (prc != null && !prcs.contains(prc))
            prcs.add(prc);
    }

    public void removePRC(PRC prc) {
        if (prc != null && prcs.contains(prc))
            prcs.remove(prc);
    }

    public PRC getActivePRC() {
        for (PRC prc : prcs) {
            if (prc.isActive())
                return prc;
        }
        return null;
    }
}
