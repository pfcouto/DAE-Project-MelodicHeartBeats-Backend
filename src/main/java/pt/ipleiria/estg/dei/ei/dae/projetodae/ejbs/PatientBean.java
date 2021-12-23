package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String birthDate, String email, String phoneNumber) throws MyEntityExistsException, MyConstraintViolationException {
        Patient newPatient = findPatient(username);

        if (newPatient != null){
            throw new MyEntityExistsException("Patient with username: " + username + " already exists");
        }

        try {
            newPatient = new Patient(username, password, name, birthDate, email, phoneNumber);
            em.persist(newPatient);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Patient> getAllPatients() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Patient>) em.createNamedQuery("getAllPatients").getResultList();
    }

    public List<Patient> getAllPatientsNotDeleted() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Patient>) em.createNamedQuery("getAllPatientsNotDeleted").getResultList();
    }

    public Patient findPatient(String username) {
        return em.find(Patient.class, username);
    }

    public void deletePatient(String username) {
        Patient patient = findPatient(username);
        if (patient != null) {
//            em.remove(patient);
            patient.setDeleted(true);
        }
    }

    public void updatePatient(String username, String name, String birthDate, String email, String phoneNumber) throws MyEntityNotFoundException {
        Patient patient = em.find(Patient.class, username);
        if (patient == null){
            throw new MyEntityNotFoundException("Patient" + username + "t NOT FOUND");
        }
        em.lock(patient, LockModeType.OPTIMISTIC);
        patient.setName(name);
        patient.setEmail(email);
        patient.setPhoneNumber(phoneNumber);
        patient.setBirthDate(birthDate);
    }
}
