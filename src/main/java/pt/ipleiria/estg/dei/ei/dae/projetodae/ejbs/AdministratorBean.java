package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class AdministratorBean {

    @PersistenceContext
    EntityManager em;

    public void create (String username, String password, String name, Date birthDate, String email, int phoneNumber){
        Administrator administrator = new Administrator(username, password, name, birthDate, email, phoneNumber);
        em.persist(administrator);
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

    public void updateAdministrator(String username, String password, String name, String email) {
        Administrator administrator = em.find(Administrator.class, username);
        administrator.setPassword(password);
        administrator.setName(name);
        administrator.setEmail(email);
    }
}
