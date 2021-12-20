package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@Stateless
public class AdministratorBean {

    @PersistenceContext
    EntityManager em;

    public void create (String username, String password, String name, String email, String phoneNumber) throws MyEntityExistsException, MyConstraintViolationException {
        Administrator newAdmin = findAdministrator(username);

        if (newAdmin != null) {
            throw new MyEntityExistsException("Administrator with username: " + username + " already exists");
        }

        try {
            newAdmin = new Administrator(username, password, name, email, phoneNumber);
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
        }
    }

    public void updateAdministrator(String username, String password, String name, String email, String phoneNumber) {
        Administrator administrator = em.find(Administrator.class, username);
        administrator.setPassword(password);
        administrator.setName(name);
        administrator.setEmail(email);
        administrator.setPhoneNumber(phoneNumber);
    }
}
