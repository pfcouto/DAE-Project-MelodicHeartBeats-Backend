package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPRCs",
                query = "SELECT p FROM PRC p ORDER BY p.id" // JPQL
        )
})
public class PRC implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    private Patient patient;
    @OneToMany(mappedBy = "prc", cascade = CascadeType.REMOVE)
    private LinkedList<Prescription> prescriptions;
    @NotNull
    private Calendar startDate;
    @NotNull
    private Calendar endDate;
    @NotNull
    private boolean active;
    @Version
    private int version;

    public PRC() {
        prescriptions = new LinkedList<>();
    }

    public PRC(Patient patient, String startDate, String endDate) {
        this.patient = patient;

        String[] date = startDate.split("-");
        this.startDate = Calendar.getInstance();
        this.startDate.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));

        date = endDate.split("-");
        this.endDate = Calendar.getInstance();
        this.endDate.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));

        this.active = true;
        prescriptions = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LinkedList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(LinkedList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
