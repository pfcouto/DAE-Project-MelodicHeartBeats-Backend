package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.util.LinkedList;
import java.util.List;

public class DoctorDTO {
    String username;
    String password;
    String name;
    String birthDate;
    String email;
    String phoneNumber;
    String office;

    List<PrescriptionDTO> prescriptionDTOS;

    public DoctorDTO() {
        prescriptionDTOS = new LinkedList<>();
    }

    public DoctorDTO(String username, String password, String name, String birthDate, String email, String phoneNumber, String office) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.office = office;
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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public List<PrescriptionDTO> getPrescriptionDTOS() {
        return prescriptionDTOS;
    }

    public void setPrescriptionDTOS(List<PrescriptionDTO> prescriptionDTOS) {
        this.prescriptionDTOS = prescriptionDTOS;
    }
}
