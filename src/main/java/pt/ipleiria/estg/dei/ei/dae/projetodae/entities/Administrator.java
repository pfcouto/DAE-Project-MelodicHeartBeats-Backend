package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.LinkedList;

@Entity
public class Administrator extends User implements Serializable {

    private LinkedList<BiometricsType>

    public Administrator() {
    }
}
