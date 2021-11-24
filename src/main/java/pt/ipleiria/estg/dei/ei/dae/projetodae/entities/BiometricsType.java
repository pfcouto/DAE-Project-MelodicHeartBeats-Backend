package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
//import java.util.LinkedHashSet;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricsTypes",
                query = "SELECT b FROM BiometricsType b ORDER BY b.code" // JPQL
        )
})
public class BiometricsType implements Serializable {
    @Id
    private int code;
    @NotNull
    private String type;
    @NotNull
    private int max;
    @NotNull
    private int min;
    @NotNull
    private String unity;
    @OneToMany(mappedBy = "biometricsType")
    private LinkedList<Biometric> biometrics;

    @ManyToOne
    @JoinColumn(name = "ADMINISTRATOR_USERNAME")
    @NotNull
    private Administrator administrator;

    public BiometricsType() {
        biometrics = new LinkedList<>();
    }

    public BiometricsType(int code, String type, int max, int min, String unity, Administrator administrator) {
        this.code = code;
        this.type = type;
        this.max = max;
        this.min = min;
        this.unity = unity;
        this.administrator = administrator;
        biometrics = new LinkedList<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public LinkedList<Biometric> getBiometrics() {
        return biometrics;
    }

    public void setBiometrics(LinkedList<Biometric> biometrics) {
        this.biometrics = biometrics;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
}
