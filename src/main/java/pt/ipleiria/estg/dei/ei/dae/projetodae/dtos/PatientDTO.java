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
    boolean blocked;
    List<PrescriptionDTO> prescriptionDTOS;
    List<ObservationDTO> observationDTOS;
    List<PRCDTO> prcdtos;

    public PatientDTO() {
        prescriptionDTOS = new LinkedList<>();
        observationDTOS=new LinkedList<>();
        prcdtos=new LinkedList<>();
    }

    public PatientDTO(String username, String password, String name, String birthDate, String email, String phoneNumber, boolean blocked) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.blocked = blocked;
        prescriptionDTOS = new LinkedList<>();
        observationDTOS=new LinkedList<>();
        prcdtos=new LinkedList<>();
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

    public List<ObservationDTO> getObservationDTOS() {
        return observationDTOS;
    }

    public void setObservationDTOS(List<ObservationDTO> observationDTOS) {
        this.observationDTOS = observationDTOS;
    }

    public List<PRCDTO> getPrcdtos() {
        return prcdtos;
    }

    public void setPrcdtos(List<PRCDTO> prcdtos) {
        this.prcdtos = prcdtos;
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
