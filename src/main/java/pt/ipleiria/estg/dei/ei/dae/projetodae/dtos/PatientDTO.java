package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PatientDTO {
    String username;
    String password;
    String name;
//    Date birthDate;
    String email;
    String phoneNumber;
    List<PresciptionDTO> presciptionDTOS;

    public PatientDTO() {
        this.presciptionDTOS = new LinkedList<>();
    }

    public PatientDTO(String username, String password, String name, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
//        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.presciptionDTOS = new LinkedList<>();
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

    public List<PresciptionDTO> getPresciptionDTOS() {
        return presciptionDTOS;
    }

    public void setPresciptionDTOS(List<PresciptionDTO> presciptionDTOS) {
        this.presciptionDTOS = presciptionDTOS;
    }
//
//    public Date getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(Date birthDate) {
//        this.birthDate = birthDate;
//    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
