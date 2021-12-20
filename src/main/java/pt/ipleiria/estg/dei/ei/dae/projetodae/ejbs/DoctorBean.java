package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import java.util.Date;
import java.util.List;

@Stateless
public class DoctorBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String birthDate, String email, String phoneNumber, String office){
        Doctor doctor = new Doctor(username, password, name, birthDate, email, phoneNumber, office);
        em.persist(doctor);
    }

    public List<Doctor> getAllDoctors() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Doctor>) em.createNamedQuery("getAllDoctors").getResultList();
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

    public void updateDoctor(String username, String password, String name, String birthDate, String email, String phoneNumber, String office) {
        Doctor doctor = em.find(Doctor.class, username);
        doctor.setPassword(password);
        doctor.setName(name);
        doctor.setBirthDate(birthDate);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phoneNumber);
        doctor.setOffice(office);
    }
}
