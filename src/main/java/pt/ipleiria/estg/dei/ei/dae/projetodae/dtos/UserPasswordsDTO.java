package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

public class UserPasswordsDTO {
    String passwordOld;
    String passwordNew;

    public UserPasswordsDTO() {
    }

    public UserPasswordsDTO(String passwordOld, String passwordNew) {
        this.passwordOld = passwordOld;
        this.passwordNew = passwordNew;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }
}


