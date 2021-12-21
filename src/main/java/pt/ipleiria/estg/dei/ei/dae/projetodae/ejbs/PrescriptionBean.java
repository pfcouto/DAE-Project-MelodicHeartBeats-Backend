package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    EntityManager em;

    public void create(String doctorUsername, String patientUsername, String description, String startDate, String endDate) {
        Doctor doctor = findDoctor(doctorUsername);
        Patient patient = findPatient(patientUsername);
        Prescription prescription = new Prescription(doctor, patient, description, startDate, endDate);
        em.persist(prescription);
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

}
