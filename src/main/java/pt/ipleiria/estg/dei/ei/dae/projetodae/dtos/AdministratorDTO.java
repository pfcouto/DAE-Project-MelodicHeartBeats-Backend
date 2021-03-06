package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.util.LinkedList;
import java.util.List;

public class AdministratorDTO {
    String username;
    String password;
    String name;
    String birthDate;
    String email;
    String phoneNumber;
    boolean blocked;
    List<BiometricsTypeDTO> biometricsTypeDTOS;

    public AdministratorDTO() {
        biometricsTypeDTOS = new LinkedList<>();
    }

    public AdministratorDTO(String username, String password, String name, String birthDate, String email, String phoneNumber, boolean blocked) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.blocked = blocked;
        biometricsTypeDTOS = new LinkedList<>();
    }

    public AdministratorDTO(String username, String name, String email, String phoneNumber) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    public List<BiometricsTypeDTO> getBiometricsTypeDTOS() {
        return biometricsTypeDTOS;
    }

    public void setBiometricsTypeDTOS(List<BiometricsTypeDTO> biometricsTypeDTOS) {
        this.biometricsTypeDTOS = biometricsTypeDTOS;
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
