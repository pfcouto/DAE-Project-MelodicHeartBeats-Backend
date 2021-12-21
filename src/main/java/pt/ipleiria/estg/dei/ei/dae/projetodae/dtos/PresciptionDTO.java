package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

public class PresciptionDTO {
    private int id;
    private String doctor;
    private String patient;
    private String description;
    private String startDate;
    private String endDate;

    public PresciptionDTO() {
    }

    public PresciptionDTO(int id, String doctor, String patient, String description, String startDate, String endDate) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
