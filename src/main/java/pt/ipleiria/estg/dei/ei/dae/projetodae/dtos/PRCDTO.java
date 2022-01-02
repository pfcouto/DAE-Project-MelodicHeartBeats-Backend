package pt.ipleiria.estg.dei.ei.dae.projetodae.dtos;

import java.util.LinkedList;
import java.util.List;

public class PRCDTO {
    int id;
    String patient;
    String startDate;
    String endDate;
    Boolean active;
    List<PrescriptionDTO> prescriptions;

    public PRCDTO() {
        prescriptions = new LinkedList<>();
    }

    public PRCDTO(int id, String patient, String startDate, String endDate, Boolean active) {
        this.id = id;
        this.patient = patient;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        prescriptions = new LinkedList<>();
    }

    public List<PrescriptionDTO> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionDTO> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
