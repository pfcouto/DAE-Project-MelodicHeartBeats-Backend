package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    EntityManager em;

    public int create(String doctorUsername, String patientUsername, String description, String startDate, String endDate) throws MyEntityNotFoundException {
        Doctor doctor = findDoctor(doctorUsername);
        Patient patient = findPatient(patientUsername);
        PRC activePrc = patient.getActivePRC();
        if (activePrc == null) {
            throw new MyEntityNotFoundException("Active PRC not found");
        }
        Prescription prescription = new Prescription(doctor, patient, activePrc, description, startDate, endDate);
        em.persist(prescription);
        return prescription.getId();
    }

    public List<Prescription> getAllPrescriptions() {
        return (List<Prescription>) em.createNamedQuery("getAllPrescriptions").getResultList();
    }

    public Prescription findPrescription(int id) {
        return em.find(Prescription.class, id);
    }

    public Doctor findDoctor(String username) {
        return em.find(Doctor.class, username);
    }

    public Patient findPatient(String username) {
        return em.find(Patient.class, username);
    }

    public void deletePrescription(int id) throws MyEntityNotFoundException {
        Prescription prescription = findPrescription(id);
        if (prescription == null) {
            throw new MyEntityNotFoundException("Prescription " + id + " does not exist");
        }
        unassignPrescriptionFromPatientAndDoctor(id);
        em.remove(prescription);
    }

    public void updatePrescription(int id, String description, String startDate, String endDate) throws MyEntityNotFoundException {
        Prescription prescription = em.find(Prescription.class, id);
        if (prescription == null) {
            throw new MyEntityNotFoundException();
        }
        prescription.setDescription(description);
        prescription.setStartDate(startDate);
        prescription.setEndDate(endDate);
    }

    public void assignPrescriptionToPatientAndDoctor(String doctor_username,
                                                     String patient_username,
                                                     int prescription_id) throws MyEntityNotFoundException {
        Doctor doctor = findDoctor(doctor_username);
        if (doctor == null) {
            throw new MyEntityNotFoundException("Doctor " + doctor_username + " does not exist");
        }

        Patient patient = findPatient(patient_username);
        if (patient == null) {
            throw new MyEntityNotFoundException("Doctor " + patient_username + " does not exist");
        }

        Prescription prescription = findPrescription(prescription_id);
        if (prescription == null) {
            throw new MyEntityNotFoundException("Prescription " + prescription_id + " does not exist");
        }

        doctor.addPrescription(prescription);
        patient.addPrescription(prescription);
    }

    public void unassignPrescriptionFromPatientAndDoctor(int prescription_id) throws MyEntityNotFoundException {
        Prescription prescription = findPrescription(prescription_id);
        if (prescription == null) {
            throw new MyEntityNotFoundException("Prescription " + prescription_id + " does not exist");
        }

        Doctor doctor = findDoctor(prescription.getDoctor().getUsername());
        if (doctor == null) {
            throw new MyEntityNotFoundException("Doctor " + prescription.getDoctor().getUsername() + " does not exist");
        }

        Patient patient = findPatient(prescription.getPatient().getUsername());
        if (patient == null) {
            throw new MyEntityNotFoundException("Doctor " + prescription.getPatient().getUsername() + " does not exist");
        }

        prescription.setPatient(null);
        prescription.setDoctor(null);
        patient.removePrescription(prescription);
        doctor.removePrescription(prescription);
    }

}
