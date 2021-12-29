package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM  Administrator a order by a.name"
        ),
})
public class Administrator extends User implements Serializable {

    @OneToMany(mappedBy = "administrator", cascade = CascadeType.REMOVE)
    private LinkedList<BiometricsType> biometricsTypes;

    public Administrator() {
        super();
        biometricsTypes = new LinkedList<>();
    }

    public Administrator(String username, String password, String name, String birthDate, String email, String phoneNumber) {
        super(username, password, name, birthDate, email, phoneNumber);
        biometricsTypes = new LinkedList<>();
    }

    public LinkedList<BiometricsType> getBiometricsTypes() {
        return biometricsTypes;
    }

    public void setBiometricsTypes(LinkedList<BiometricsType> biometricsTypes) {
        this.biometricsTypes = biometricsTypes;
    }

    public void addBiometricsType(BiometricsType newType){
        for (BiometricsType type : biometricsTypes) {
            if (type.equals(newType)){
                //throw new exception();
                //remove return after excpetion implementation
                return;
            }
        }
        biometricsTypes.add(newType);
    }

    public void removeBiometricType(BiometricsType biometricsType) {
        this.biometricsTypes.remove(biometricsType);
    }
}
