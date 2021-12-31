package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.io.Serializable;

public class ObservationDTO implements Serializable {
    public int code;
    public String date;
    public String patient;
    public int biometricType;
    public String biometricTypeName;
    public int quantitativeValue = -1;
    public String qualitativeValue;
    public String what;
    public String local;


    public ObservationDTO() {
    }

    public ObservationDTO(int code, String date, String patient, int biometricType, String biometricTypeName, int quantitativeValue, String qualitativeValue, String what, String local) {
        this.code = code;
        this.date = date;
        this.patient = patient;
        this.biometricType = biometricType;
        this.biometricTypeName = biometricTypeName;
        this.quantitativeValue = quantitativeValue;
        this.qualitativeValue = qualitativeValue;
        this.what = what;
        this.local = local;
    }

    public int getBiometricType() {
        return biometricType;
    }

    public void setBiometricType(int biometricType) {
        this.biometricType = biometricType;
    }

    public String getBiometricTypeName() {
        return biometricTypeName;
    }

    public void setBiometricTypeName(String biometricTypeName) {
        this.biometricTypeName = biometricTypeName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public int getQuantitativeValue() {
        return quantitativeValue;
    }

    public void setQuantitativeValue(int quantitativeValue) {
        this.quantitativeValue = quantitativeValue;
    }

    public String getQualitativeValue() {
        return qualitativeValue;
    }

    public void setQualitativeValue(String qualitativeValue) {
        this.qualitativeValue = qualitativeValue;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
