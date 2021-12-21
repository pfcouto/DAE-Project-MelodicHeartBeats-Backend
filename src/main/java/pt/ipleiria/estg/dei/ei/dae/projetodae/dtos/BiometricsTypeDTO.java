package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Biometric;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;

public class BiometricsTypeDTO implements Serializable {
    public int code;
    public String type;
    public int max=-1;
    public int min=-1;
    public String unity;
    public LinkedList<BiometricDTO> biometricsDTO;
    public String admin;
    public boolean delete;


    public BiometricsTypeDTO() {
        this.biometricsDTO=new LinkedList<>();
    }

    public BiometricsTypeDTO(int code, String type, int max, int min, String unity, String administratorUsername,boolean delete) {
        this.code = code;
        this.type = type;
        this.max = max;
        this.min = min;
        this.unity = unity;
        this.delete=delete;
        this.admin = administratorUsername;
        this.biometricsDTO=new LinkedList<>();
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
