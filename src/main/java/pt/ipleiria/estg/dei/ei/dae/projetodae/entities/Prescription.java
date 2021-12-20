package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPrescriptions",
                query = "SELECT p FROM Prescription p ORDER BY p.id" // JPQL
        )
})
public class Prescription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Calendar startDate;
    private Calendar endDate;

    public Prescription() {
    }

    public Prescription(Doctor doctor, Patient patient, String description, String startDate, String endDate) {
        this.doctor = doctor;
        this.patient = patient;
        this.description = description;

        String[] startDateStrings = startDate.split("-");
        this.startDate = Calendar.getInstance();
        this.startDate.set(Integer.parseInt(startDateStrings[0]), Integer.parseInt(startDateStrings[1]) - 1, Integer.parseInt(startDateStrings[2]));

        String[] endDateStrings = endDate.split("-");
        this.endDate = Calendar.getInstance();
        this.endDate.set(Integer.parseInt(endDateStrings[0]), Integer.parseInt(endDateStrings[1]) - 1, Integer.parseInt(endDateStrings[2]));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(startDate.getTime());
    }

    public void setStartDate(String startDate) {
        String[] date = startDate.split("-");
        this.startDate = Calendar.getInstance();
        this.startDate.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
    }

    public String getEndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(endDate.getTime());
    }

    public void setEndDate(String endDate) {
        String[] date = endDate.split("-");
        this.endDate = Calendar.getInstance();
        this.endDate.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
    }
}
