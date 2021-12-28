package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class PatientBean {

    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String birthDate, String email, String phoneNumber) throws MyEntityExistsException, MyConstraintViolationException {
        Patient newPatient = findPatient(username);

        if (newPatient != null) {
            throw new MyEntityExistsException("Patient with username: " + username + " already exists" );
        }

        try {
            newPatient = new Patient(username, password, name, birthDate, email, phoneNumber);
            em.persist(newPatient);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Patient> getAllPatients() {
        return (List<Patient>) em.createNamedQuery("getAllPatients" ).getResultList();
    }

    public List<Patient> getAllPatientsNotDeleted() {
        return (List<Patient>) em.createNamedQuery("getAllPatientsNotDeleted" ).getResultList();
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

    public void blockOrUnBlockPatient(String username) {
        Patient patient = findPatient(username);
        if (patient != null) {
            patient.setBlocked(!patient.isBlocked());
        }
    }

    public void updatePatient(String username, String name, String birthDate, String email, String phoneNumber) throws MyEntityNotFoundException {
        Patient patient = em.find(Patient.class, username);
        if (patient == null) {
            throw new MyEntityNotFoundException("Patient" + username + "t NOT FOUND" );
        }
        em.lock(patient, LockModeType.OPTIMISTIC);
        patient.setName(name);
        patient.setEmail(email);
        patient.setPhoneNumber(phoneNumber);
        patient.setBirthDate(birthDate);
    }

    public List<Prescription> getPrescriptions(Patient patient) {
        Query query = em.createNamedQuery("getPrescriptionsOfPatient" );
        query.setParameter("username", patient.getUsername());
        return query.getResultList();
    }

    public boolean changePasswordPatient(String username, String passwordOld, String passwordNew) throws MyEntityNotFoundException {
        Patient patient = findPatient(username);
        if (patient == null) {
            throw new MyEntityNotFoundException("Patient" + username + " NOT FOUND" );
        }
        return patient.changePassword(passwordOld, passwordNew);
    }
}
