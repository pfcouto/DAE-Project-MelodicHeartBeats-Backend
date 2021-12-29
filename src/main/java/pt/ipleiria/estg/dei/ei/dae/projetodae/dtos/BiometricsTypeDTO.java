package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BiometricsTypeDTO implements Serializable {
    public int code;
    public String name;
    public String description;
    public int valueMax=-1;
    public int valueMin =-1;
    public String unity;
    public List<QualitativeValuesDTO> qualitatives;
    public String admin;
    public String deleted_at;


    public BiometricsTypeDTO() {
        qualitatives=new LinkedList<>();
    }

    public BiometricsTypeDTO(int code, String name,String description, int valueMax, int valueMin, String unity, String administratorUsername, String deleted_at,List<QualitativeValuesDTO> qualitativeValuesDTOS) {
        this.code = code;
        this.name = name;
        this.description=description;
        this.valueMax = valueMax;
        this.valueMin = valueMin;
        this.unity = unity;
        this.deleted_at=deleted_at;
        this.admin = administratorUsername;
        qualitatives=qualitativeValuesDTOS;
    }

    public List<QualitativeValuesDTO> getQualitatives() {
        return qualitatives;
    }

    public void setQualitatives(List<QualitativeValuesDTO> qualitatives) {
        this.qualitatives = qualitatives;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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

    public void setName(String name) {
        this.name = name;
    }

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }

    public int getValueMin() {
        return valueMin;
    }

    public void setValueMin(int valueMin) {
        this.valueMin = valueMin;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getAdministrator() {
        return admin;
    }

    public void setAdministratorDTO(String administratorUsername) {
        this.admin = administratorUsername;
    }
}
