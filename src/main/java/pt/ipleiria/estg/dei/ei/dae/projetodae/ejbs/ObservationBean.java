package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.ObservationDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Stateless
public class ObservationBean {
    @PersistenceContext
    EntityManager em;


    public Observation create(String date, String patientUsername, int biometricName, int quantitativeValue, String qualitativeValue, String what, String local){
        BiometricsType biometricsType=em.find(BiometricsType.class,biometricName);
        System.out.println(biometricsType);
        Patient patient=em.find(Patient.class,patientUsername);
        if(biometricsType!=null){
            Observation observation = new Observation(date,patient,biometricsType,quantitativeValue,qualitativeValue,what,local);
            em.persist(observation);
            System.out.println(observation);
            return observation;
        }
        System.out.println("aqui nao");
        return null;
    }

    public Observation find(int code) {
        return em.find(Observation.class,code);
    }

    public List<Observation> getAllObservations() {
        return (List<Observation>) em.createNamedQuery("getAllObservations").getResultList();
    }

    public boolean update(int code, ObservationDTO observationDTO) {
        Observation observation=em.find(Observation.class,code);
        if(observation!=null){
            if(observationDTO.getLocal()!=null){
                observation.setLocal(observationDTO.getLocal());
            }
            if(observationDTO.getQuantitativeValue()!=-1){

                    observation.setQuantitativeValue(observationDTO.getQuantitativeValue());

            }
            if(observationDTO.getQualitativeValue()!=null){

                    observation.setQualitativeValue(observationDTO.getQualitativeValue());

            }
            if(observationDTO.getWhat()!=null){
                observation.setWhat(observationDTO.getWhat());
            }
        }else{
            return false;
        }
        em.merge(observation);
        return true;
    }
}
