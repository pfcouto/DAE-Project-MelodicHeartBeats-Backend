package pt.ipleiria.estg.dei.ei.dae.projetodae.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
//import java.util.LinkedHashSet;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllBiometricsTypes",
                query = "SELECT b FROM BiometricsType b ORDER BY b.code" // JPQL
        ),
        @NamedQuery(name="getAllBiometricsTypesNonDeleted",
                    query="SELECT b FROM BiometricsType b WHERE b.deleted=false ORDER BY b.code"
        )
})
public class BiometricsType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private int valueMax;
    @NotNull
    private int valueMin;
    @NotNull
    private String unity;
    @ElementCollection
    private Map<Integer,String> listOfQualitativeValues;
    @OneToMany(mappedBy = "biometricsType")
    private LinkedList<Observation> observations;
    private boolean deleted = Boolean.FALSE;
    @ManyToOne
    @JoinColumn(name = "ADMINISTRATOR_USERNAME")
    @NotNull
    private Administrator administrator;
    @Version
    private int version;

    public BiometricsType() {
        observations = new LinkedList<>();
        listOfQualitativeValues=new HashMap<Integer,String>();
    }

    public BiometricsType(String name,String description, int valueMax, int valueMin, String unity, Administrator administrator) {
        this.name = name;
        this.description=description;
        this.valueMax = valueMax;
        this.valueMin = valueMin;
        this.unity = unity;
        this.administrator = administrator;
        observations = new LinkedList<>();
        listOfQualitativeValues=new HashMap<Integer,String>();
    }


    public Map<Integer, String> getListOfQualitativeValues() {
        return listOfQualitativeValues;
    }

    public void setListOfQualitativeValues(Map<Integer, String> listOfQualitativeValues) {
        this.listOfQualitativeValues = listOfQualitativeValues;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = type;
    }

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMax(int max) {
        this.valueMax = max;
    }

    public int getValueMin() {
        return valueMin;
    }

    public void setValueMin(int min) {
        this.valueMin = min;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public LinkedList<Observation> getObservations() {
        return observations;
    }

    public void setObservations(LinkedList<Observation> observations) {
        this.observations = observations;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BiometricsType)) return false;
        BiometricsType that = (BiometricsType) o;
        return getCode() == that.getCode() && getValueMax() == that.getValueMax() && getValueMin() == that.getValueMin() && Objects.equals(getName(), that.getName()) && Objects.equals(getUnity(), that.getUnity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getName(), getValueMax(), getValueMin(), getUnity());
    }
}
