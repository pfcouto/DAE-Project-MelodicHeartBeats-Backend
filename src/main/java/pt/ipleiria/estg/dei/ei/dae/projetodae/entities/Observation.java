package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllObservations",
                query = "SELECT b from Observation b order by b.date"
        ),
        @NamedQuery(
                name = "getObservationsOfPatient",
                query = "SELECT b FROM Observation b WHERE b.patient.username = :username")
})
public class Observation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private Calendar date;
    @NotNull
    @ManyToOne
    private Patient patient;
    @NotNull
    @ManyToOne
    private BiometricsType biometricsType;
    @NotNull
    private int quantitativeValue;
    private String qualitativeValue;
    @NotNull
    private String what;
    @NotNull
    private String local;
    @Version
    private int version;

    public Observation() {
    }

    public Observation(String date, Patient patient, BiometricsType biometricsType, int quantitativeValue, String qualitativeValue, String what, String local) {
        String[] ArrayDate = date.split("-");
        this.date = Calendar.getInstance();
        this.date.set(Integer.parseInt(ArrayDate[0]), Integer.parseInt(ArrayDate[1]) - 1, Integer.parseInt(ArrayDate[2]));
        this.patient = patient;
        this.biometricsType = biometricsType;
        this.quantitativeValue = quantitativeValue;
        this.qualitativeValue = qualitativeValue;
        this.what = what;
        this.local = local;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQualitativeValue() {
        return qualitativeValue;
    }

    public void setQualitativeValue(String qualitativeValue) {
        this.qualitativeValue = qualitativeValue;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date.getTime());
    }

    public void setDate(String date) {
        String[] ArrayDate = date.split("-");
        this.date = Calendar.getInstance();
        this.date.set(Integer.parseInt(ArrayDate[0]), Integer.parseInt(ArrayDate[1]) - 1, Integer.parseInt(ArrayDate[2]));
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

    public int getQuantitativeValue() {
        return quantitativeValue;
    }

    public void setQuantitativeValue(int value) {
        this.quantitativeValue = value;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String source) {
        this.local = source;
    }
}
