package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import java.util.Date;
import java.util.List;

@Stateless
public class PrescriptionBean {
    @PersistenceContext
    EntityManager em;

    public void create(String doctorUsername, String patientUsername, String description) {
        Doctor doctor = findDoctor(doctorUsername);
        Patient patient = findPatient(patientUsername);
        Prescription prescription = new Prescription(doctor, patient, description);
        em.persist(prescription);
    }

    public List<Doctor> getAllPrescriptions() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Doctor>) em.createNamedQuery("getAllDoctors").getResultList();
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

//    public void updatePrescription(String username, String password, String name, Date birthDate, String email, int phoneNumber, String office) {
//        Doctor doctor = em.find(Doctor.class, username);
//        doctor.setPassword(password);
//        doctor.setName(name);
//        doctor.setBirthDate(birthDate);
//        doctor.setEmail(email);
//        doctor.setPhoneNumber(phoneNumber);
//        doctor.setOffice(office);
//    }

}
