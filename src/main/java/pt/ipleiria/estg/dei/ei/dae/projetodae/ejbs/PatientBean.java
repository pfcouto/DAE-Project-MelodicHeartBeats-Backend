package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, Date birthDate, String email, int phoneNumber) {
        Patient patient = new Patient(username, password, name, birthDate, email, phoneNumber);
        em.persist(patient);
    }

    public List<Patient> getAllPatients() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Patient>) em.createNamedQuery("getAllPatients").getResultList();
    }

    public Patient findPatient(String username) {
        return em.find(Patient.class, username);
    }

    public void deletePatient(String username) {
        Patient patient = findPatient(username);
        if (patient != null) {
            em.remove(patient);
        }
    }

    public void updatePatient(String username, String password, String name, Date birthDate, String email, int phoneNumber) {
        Patient patient = em.find(Patient.class, username);
        patient.setPassword(password);
        patient.setName(name);
        patient.setBirthDate(birthDate);
        patient.setEmail(email);
        patient.setPhoneNumber(phoneNumber);
    }
}
