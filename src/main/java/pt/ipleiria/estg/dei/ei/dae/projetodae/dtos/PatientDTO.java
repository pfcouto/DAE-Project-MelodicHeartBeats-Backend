package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.util.LinkedList;
import java.util.List;

public class PatientDTO {
    String username;
    String password;
    String name;
    String birthDate;
    String email;
    String phoneNumber;
    boolean isDeleted;
    List<PrescriptionDTO> prescriptionDTOS;

    public PatientDTO() {
        prescriptionDTOS = new LinkedList<>();
    }

    public PatientDTO(String username, String password, String name, String birthDate, String email, String phoneNumber, boolean isDeleted) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isDeleted = isDeleted;
        prescriptionDTOS = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PrescriptionDTO> getPrescriptionDTOS() {
        return prescriptionDTOS;
    }

    public void setPrescriptionDTOS(List<PrescriptionDTO> prescriptionDTOS) {
        this.prescriptionDTOS = prescriptionDTOS;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
