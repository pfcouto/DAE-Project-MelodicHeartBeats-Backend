package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
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

        patient.addPRC(prc);
        em.merge(patient);
    }

    public List<PRC> getAllprcs() {
        return em.createNamedQuery("getAllPRCs").getResultList();
    }

    public boolean updateActive(int code, Boolean active) throws MyEntityNotFoundException {
        PRC prc = findPRC(code);
        if (prc == null) {
            throw new MyEntityNotFoundException();
        }
        Patient patient = prc.getPatient();
        if (active && patient.getActivePRC() != null && !patient.getActivePRC().equals(prc)) {
            throw new IllegalArgumentException("Patient has already an active PRC");
        }
        prc.setActive(active);
        em.persist(prc);
        return true;
    }

    public PRC findPRC(int prcId) {
        return em.find(PRC.class, prcId);
    }

    public boolean updateEndDate(int code, String endDate) throws MyEntityNotFoundException {
        PRC prc = findPRC(code);
        if (prc == null) {
            throw new MyEntityNotFoundException();
        }

        // End Date PRC into calendar
        Calendar endDatePRC = Calendar.getInstance();
        String[] endDatePRCArray = endDate.split("-");
        endDatePRC.set(Integer.parseInt(endDatePRCArray[0]), Integer.parseInt(endDatePRCArray[1]) - 1, Integer.parseInt(endDatePRCArray[2]));

        for (Prescription prescription : prc.getPrescriptions()) {
            // End Date into calendar
            Calendar endDatePrescription = Calendar.getInstance();
            String[] endDatePrescriptionArray = prescription.getEndDate().split("-");
            endDatePrescription.set(Integer.parseInt(endDatePrescriptionArray[0]), Integer.parseInt(endDatePrescriptionArray[1]) - 1, Integer.parseInt(endDatePrescriptionArray[2]));

            if (endDatePRC.before(endDatePrescription)) {
                throw new IllegalArgumentException("PRC has prescriptions ending later than end date inserted");
            }
        }
        prc.setEndDate(endDate);
        em.persist(prc);
        return true;
    }
}
