package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllRules",
                query = "SELECT r FROM Rule r ORDER BY r.id" // JPQL
        )
})
public class Rule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "BIOMETRICSTYPE_ID")
    @NotNull
    private BiometricsType biometricType;
    @NotNull
    private String exp;
    @NotNull
    private int value;
    private String description;
    @NotNull
    private int days;
    @Version
    private int version;

    public Rule() {
    }

    public Rule(BiometricsType biometricType, String exp, int value, String description, int days) {
        this.biometricType = biometricType;
        this.exp = exp;
        this.value = value;
        this.description = description;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BiometricsType getBiometricType() {
        return biometricType;
    }

    public void setBiometricType(BiometricsType biometricType) {
        this.biometricType = biometricType;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
