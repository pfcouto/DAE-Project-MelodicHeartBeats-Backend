package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    EntityManager em;

    public int create(String doctorUsername, String patientUsername, String description, String startDate, String endDate) {
        Doctor doctor = findDoctor(doctorUsername);
        Patient patient = findPatient(patientUsername);
        Prescription prescription = new Prescription(doctor, patient, description, startDate, endDate);
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

    public void deletePrescription(int id) {
        Prescription prescription = findPrescription(id);
        if (prescription != null) {
            em.remove(prescription);
        }
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
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
    }

    public void unassignPrescriptionFromPatientAndDoctor(String doctor_username,
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

        prescription.setPatient(null);
        prescription.setDoctor(null);
        patient.removePrescription(prescription);
        doctor.removePrescription(prescription);
    }

}
