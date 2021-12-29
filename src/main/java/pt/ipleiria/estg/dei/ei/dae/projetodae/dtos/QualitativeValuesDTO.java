package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

public class QualitativeValuesDTO {
    public Integer value;
    public String meaning;

    public QualitativeValuesDTO() {

    }

    public QualitativeValuesDTO(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
