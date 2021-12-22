package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.io.Serializable;
import java.util.LinkedList;

public class BiometricsTypeDTO implements Serializable {
    public int code;
    public String name;
    public String description;
    public int valueMax=-1;
    public int valueMin =-1;
    public String unity;
    public LinkedList<BiometricDTO> biometricsDTO;
    public String admin;
    public boolean delete;


    public BiometricsTypeDTO() {
        this.biometricsDTO=new LinkedList<>();
    }

    public BiometricsTypeDTO(int code, String name,String description, int valueMax, int valueMin, String unity, String administratorUsername, boolean delete) {
        this.code = code;
        this.name = name;
        this.description=description;
        this.valueMax = valueMax;
        this.valueMin = valueMin;
        this.unity = unity;
        this.delete=delete;
        this.admin = administratorUsername;
        this.biometricsDTO=new LinkedList<>();
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
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

    public LinkedList<BiometricDTO> getBiometricsDTO() {
        return biometricsDTO;
    }

    public void setBiometrics(LinkedList<BiometricDTO> biometricsDTO) {
        this.biometricsDTO = biometricsDTO;
    }

    public String getAdministrator() {
        return admin;
    }

    public void setAdministratorDTO(String administratorUsername) {
        this.admin = administratorUsername;
    }
}
