package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
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
public class DoctorBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String birthDate, String email, String phoneNumber, String office) throws MyEntityExistsException, MyConstraintViolationException {

        Doctor newDoctor = findDoctor(username);

        if (newDoctor != null) {
            throw new MyEntityExistsException("Doctor with username: " + username + " already exists" );
        }
        try {
            newDoctor = new Doctor(username, password, name, birthDate, email, phoneNumber, office);
            em.persist(newDoctor);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }

    }

    public List<Doctor> getAllDoctors() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Doctor>) em.createNamedQuery("getAllDoctors" ).getResultList();
    }

    public Doctor findDoctor(String username) {
        return em.find(Doctor.class, username);
    }

    public void deleteDoctor(String username) {
        Doctor doctor = findDoctor(username);
        if (doctor != null) {
            em.remove(doctor);
        }
    }

    public void blockOrUnBlockDoctor(String username) {
        Doctor doctor = findDoctor(username);
        if (doctor != null) {
            doctor.setBlocked(!doctor.isBlocked());
        }
    }

    public void updateDoctor(String username, String name, String birthDate, String email, String phoneNumber, String office) throws MyEntityNotFoundException {
        Doctor doctor = em.find(Doctor.class, username);
        if (doctor == null) {
            throw new MyEntityNotFoundException("Doctor" + username + " NOT FOUND" );
        }
        em.lock(doctor, LockModeType.OPTIMISTIC);
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phoneNumber);
        doctor.setOffice(office);
        doctor.setBirthDate(birthDate);
    }


    public List<Prescription> getPrescriptions(Doctor doctor) {
        Query query = em.createNamedQuery("getPrescriptionsOfDoctor" );
        query.setParameter("username", doctor.getUsername());
        return query.getResultList();
    }

    public boolean changePasswordDoctor(String username, String passwordOld, String passwordNew) throws MyEntityNotFoundException {
        Doctor doctor = em.find(Doctor.class, username);
        if (doctor == null) {
            throw new MyEntityNotFoundException("Doctor" + username + " NOT FOUND" );
        }
        return doctor.changePassword(passwordOld, passwordNew);
    }
}
