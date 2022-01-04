package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

public class RuleDTO {
    private int id;
    private int biometricTypeCode;
    private String biometricTypeName;
    private String exp;
    private int value;
    private String description;
    private int days;

    public RuleDTO() {
    }

    public RuleDTO(int id, int biometricTypeCode, String biometricTypeName, String exp, int value, String description, int days) {
        this.id = id;
        this.biometricTypeCode = biometricTypeCode;
        this.biometricTypeName = biometricTypeName;
        this.exp = exp;
        this.value = value;
        this.description = description;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBiometricTypeCode() {
        return biometricTypeCode;
    }

    public void setBiometricTypeCode(int biometricTypeCode) {
        this.biometricTypeCode = biometricTypeCode;
    }

    public String getBiometricTypeName() {
        return biometricTypeName;
    }

    public void setBiometricTypeName(String biometricTypeName) {
        this.biometricTypeName = biometricTypeName;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
