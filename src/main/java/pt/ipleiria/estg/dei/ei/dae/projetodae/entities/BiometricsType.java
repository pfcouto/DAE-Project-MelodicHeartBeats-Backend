package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashSet;

@Entity
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
    private LinkedHashSet<Biometric> biometrics;
}
