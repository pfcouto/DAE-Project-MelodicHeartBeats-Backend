package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.naming.directory.InvalidAttributesException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class RuleBean {
    @PersistenceContext
    EntityManager em;

    public List<Rule> getAllRules() {
        return (List<Rule>) em.createNamedQuery("getAllRules").getResultList();
    }

    public void create(int biometricTypeCode, String exp, int value, String description, int days) throws MyEntityNotFoundException, InvalidAttributesException {
        BiometricsType biometricType = em.find(BiometricsType.class, biometricTypeCode);
        if (biometricType == null) {
            throw new MyEntityNotFoundException("Biometric Type not found");
        }
        if (value < 0) {
            throw new MyEntityNotFoundException("value must be greater than 0");
        }
        if (days < 0) {
            throw new MyEntityNotFoundException("days must be greater than 0");
        }
        if (!exp.equals("<") && !exp.equals("=") && !exp.equals(">")) {
            throw new MyEntityNotFoundException("Comparation type invalid");
        }
        Rule rule = new Rule(biometricType, exp, value, description, days);
        em.persist(rule);
    }

    public Rule findRule(int ruleId) {
        return em.find(Rule.class, ruleId);
    }

    public void deleteRule(int ruleId) throws MyEntityNotFoundException {
        Rule rule = findRule(ruleId);
        if (rule == null) {
            throw new MyEntityNotFoundException("Rule " + ruleId + " not found");
        }

        em.remove(rule);
    }

    public void updateRule(int ruleId, int biometricTypeCode, String exp, int value, String description, int days) throws MyEntityNotFoundException {
        Rule rule = findRule(ruleId);
        if (rule == null) {
            throw new MyEntityNotFoundException("Rule not found");
        }
        BiometricsType biometricType = em.find(BiometricsType.class, biometricTypeCode);
        if (biometricType == null) {
            throw new MyEntityNotFoundException("Biometric Type not found");
        }
        em.lock(rule, LockModeType.OPTIMISTIC);
        rule.setBiometricType(biometricType);
        rule.setExp(exp);
        rule.setValue(value);
        rule.setDescription(description);
        rule.setDays(days);
        em.persist(rule);
    }

    public List<Prescription> getAllPrescriptions() {
        List<Observation> allObservations = (List<Observation>) em.createNamedQuery("getAllObservations").getResultList();
        List<Prescription> prescriptions = new LinkedList<>();

        for (Observation observation : allObservations) {
            for (Rule rule : getAllRules()) {
                if (rule.getExp().equals("<") && observation.getQuantitativeValue() < rule.getValue()) {
                    prescriptions.add(getPrescriptionForRule(rule, observation.getPatient()));
                } else if (rule.getExp().equals("=") && observation.getQuantitativeValue() == rule.getValue()) {
                    prescriptions.add(getPrescriptionForRule(rule, observation.getPatient()));
                } else if (rule.getExp().equals(">") && observation.getQuantitativeValue() > rule.getValue()) {
                    prescriptions.add(getPrescriptionForRule(rule, observation.getPatient()));
                }
            }
        }

        return prescriptions;
    }

    public List<Prescription> getPrescriptionsOfPatient(String username) throws MyEntityNotFoundException {
        Patient patient = em.find(Patient.class, username);
        if (patient == null) {
            throw new MyEntityNotFoundException("Patient " + username + " not found");
        }

        Query query = em.createNamedQuery("getObservationsOfPatient");
        query.setParameter("username", username);
        List<Observation> observations = (List<Observation>) query.getResultList();

        List<Prescription> prescriptions = new LinkedList<>();

        for (Observation observation : observations) {
            for (Rule rule : getAllRules()) {
                if (rule.getExp().equals("<") && observation.getQuantitativeValue() < rule.getValue()) {
                    prescriptions.add(getPrescriptionForRule(rule, patient));
                } else if (rule.getExp().equals("=") && observation.getQuantitativeValue() == rule.getValue()) {
                    prescriptions.add(getPrescriptionForRule(rule, patient));
                } else if (rule.getExp().equals(">") && observation.getQuantitativeValue() > rule.getValue()) {
                    prescriptions.add(getPrescriptionForRule(rule, patient));
                }
            }
        }

        System.out.println("Prescriptions length in Bean: " + prescriptions.size());
        return prescriptions;
    }


    private Prescription getPrescriptionForRule(Rule rule, Patient patient) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStart = formatter.format(cal.getTime());
        cal.add(Calendar.DATE, rule.getDays());
        String formattedEnd = formatter.format(cal.getTime());

        Prescription prescription = new Prescription(null, patient, patient.getActivePRC(), rule.getDescription(), formattedStart, formattedEnd);
        return prescription;
    }
}
