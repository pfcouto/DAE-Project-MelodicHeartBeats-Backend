package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PRCBean {
    @PersistenceContext
    EntityManager em;

    public void create(String patientUsername, String startDate, String endDate) {
        Patient patient = em.find(Patient.class, patientUsername);

        if (patient.getActivePRC() != null) {
            throw new IllegalArgumentException("Patient already has an active PRC");
        }

        PRC prc = new PRC(patient, startDate, endDate);
        em.merge(prc);
    }

    public List<PRC> getAllprcs() {
        return em.createNamedQuery("getAllPRCs").getResultList();
    }
}
