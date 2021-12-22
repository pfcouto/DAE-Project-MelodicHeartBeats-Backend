package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllObservations",
                query = "SELECT b from Observation b order by b.date"
        )
})
public class Observation implements Serializable {
    @Id
    private Date date;
    @Id
    @ManyToOne
    private Patient patient;
    @Id
    @ManyToOne
    private BiometricsType biometricsType;
    @NotNull
    private int value;
    private String source;

    public Observation() {
    }

    public Observation(Date date, Patient patient, BiometricsType biometricsType, int value, String source) {
        this.date = date;
        this.patient = patient;
        this.biometricsType = biometricsType;
        this.value = value;
        this.source = source;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public BiometricsType getBiometricsType() {
        return biometricsType;
    }

    public void setBiometricsType(BiometricsType biometricsType) {
        this.biometricsType = biometricsType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
