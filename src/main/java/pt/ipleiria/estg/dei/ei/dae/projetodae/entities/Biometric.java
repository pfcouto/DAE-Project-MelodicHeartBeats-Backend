package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Biometric implements Serializable {
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
}
