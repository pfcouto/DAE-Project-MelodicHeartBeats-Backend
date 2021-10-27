package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Prescription implements Serializable {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    @NotNull
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    private Patient patient;
    private String description;

}
