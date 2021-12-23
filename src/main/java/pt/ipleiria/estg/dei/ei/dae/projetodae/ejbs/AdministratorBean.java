package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
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
public class AdministratorBean {

    @PersistenceContext
    EntityManager em;

    public void create (String username, String password, String name, String birthDate, String email, String phoneNumber) throws MyEntityExistsException, MyConstraintViolationException {
        Administrator newAdmin = findAdministrator(username);

        if (newAdmin != null) {
            throw new MyEntityExistsException("Administrator with username: " + username + " already exists");
        }

        try {
            newAdmin = new Administrator(username, password, name, birthDate, email, phoneNumber);
            em.persist(newAdmin);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);

        }
    }

    public List<Administrator> getAllAdministrators() {
        return (List<Administrator>) em.createNamedQuery("getAllAdministrators").getResultList();
    }

    public Administrator findAdministrator(String username){
        return em.find(Administrator.class, username);
    }

    public void deleteAdministrator(String username) {
        Administrator administrator = findAdministrator(username);
        if (administrator != null){
            em.remove(administrator);
//            administrator.setDeleted(true);
        }
    }

    public void softDeleteOrUndeleteAdministrator(String username) {
        Administrator administrator = findAdministrator(username);
        if (administrator != null) {
            administrator.setDeleted(!administrator.isDeleted());
        }
    }

    public void updateAdministrator(String username, String name, String birthDate, String email, String phoneNumber) throws MyEntityNotFoundException {
        Administrator administrator = em.find(Administrator.class, username);
        if (administrator == null){
            throw new MyEntityNotFoundException("Administrator" + username + " NOT FOUND");
        }
        em.lock(administrator, LockModeType.OPTIMISTIC);
        administrator.setName(name);
        administrator.setEmail(email);
        administrator.setPhoneNumber(phoneNumber);
        administrator.setBirthDate(birthDate);
    }
}
