package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class BiometricsTypeBean {
    @PersistenceContext
    EntityManager em;

    public BiometricsType create(String name,String description, int valueMax, int valueMin, String unity, String administratorUsername) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
        if(valueMax<0){
            throw new MyIllegalArgumentException("The Max Value must be bigger or equal to 0!");
        }if(valueMin<0){
            throw new MyIllegalArgumentException("The Min Value must be bigger or equal to 0!");
        }
        if(valueMax<valueMin){
            throw new MyIllegalArgumentException("The Max Value must be bigger than the Min value");
        }
        Administrator administrator=em.find(Administrator.class,administratorUsername);
        if(administrator!=null){
            try {
                BiometricsType biometricsType = new BiometricsType(name,description,valueMax,valueMin,unity,administrator);
                em.persist(biometricsType);
                administrator.addBiometricsType(biometricsType);
                em.merge(administrator);
                return biometricsType;
            }
            catch (PersistenceException e) {
                throw new PersistenceException(e);
            }
        }else{
            throw new MyEntityNotFoundException("The administrator doesn't exist");
        }
    }

    public List<BiometricsType> getAllBiometricsTypes() {
        return (List<BiometricsType>) em.createNamedQuery("getAllBiometricsTypes").getResultList();
    }

    public BiometricsType find(int code) throws MyEntityNotFoundException{
        BiometricsType biometricsType= em.find(BiometricsType.class,code);
        if(biometricsType!=null){
            return biometricsType;
        }
        else {
            throw new MyEntityNotFoundException("The Biometric Type has not found");
        }
    }

    public void delete(BiometricsType biometricsType) {
        try{
            if (!em.contains(biometricsType)) {
                biometricsType = em.merge(biometricsType);
            }
            biometricsType.getAdministrator().removeBiometricType(biometricsType);
            em.persist(biometricsType.getAdministrator());
        }catch(PersistenceException exception){
            throw new PersistenceException(exception);
        }
        //get a list of Biometrics by Type.
        String queryString = "select b from Observation b where b.biometricsType=:code";
        Query query=em.createQuery( queryString );
        query.setParameter("code",biometricsType);
        List<Observation> observations =(List<Observation>) query.getResultList();
    try{
        if(observations.isEmpty()){
            //hard Delete
            em.remove(biometricsType);
        }else{
            //soft delete
            biometricsType.setDeleted(!biometricsType.isDeleted());
            em.persist(biometricsType);
        }
    } catch (PersistenceException exception){
        throw new PersistenceException(exception);
    }
    }

    public boolean update(int code, BiometricsTypeDTO biometricsTypeDTO) throws MyEntityNotFoundException,MyIllegalArgumentException {
        BiometricsType biometricsType=em.find(BiometricsType.class,code);
        if(biometricsType!=null){
            em.lock(biometricsType, LockModeType.OPTIMISTIC);
            if(biometricsTypeDTO.getName()!=""){
                biometricsType.setName(biometricsTypeDTO.getName());
            }else{
                throw new MyIllegalArgumentException("The Name should not be empty");
            }
            if(biometricsTypeDTO.getValueMax()!=-1){
                if(biometricsTypeDTO.getValueMax()>=biometricsType.getValueMin()){
                    biometricsType.setValueMax(biometricsTypeDTO.getValueMax());
                }else{
                    throw new MyIllegalArgumentException("The Max Value must be bigger than the Min value");
                }
            }
            if(biometricsTypeDTO.getValueMin()!=-1){
                if(biometricsType.getValueMax()>=biometricsTypeDTO.getValueMin()){
                    biometricsType.setValueMin(biometricsTypeDTO.getValueMin());
                }else{
                    throw new MyIllegalArgumentException("The Max Value must be bigger than the Min value");
                }
            }
            if(biometricsTypeDTO.getUnity()!=""){
                biometricsType.setUnity(biometricsTypeDTO.getUnity());
            }else{
                throw new MyIllegalArgumentException("The Unity should not be empty");
            }
            if(biometricsTypeDTO.getDescription().length()<255){
                biometricsType.setDescription(biometricsTypeDTO.getDescription());
            }
            else{
                throw new MyIllegalArgumentException("The description should only contain 255 characters.");
            }
        }else{
             throw new MyEntityNotFoundException("The Biometric Type has not found");
        }
        em.merge(biometricsType);
        return true;
    }

    public List<BiometricsType> getAllBiometricsTypesNonDeleted() {
        return (List<BiometricsType>) em.createNamedQuery("getAllBiometricsTypesNonDeleted").getResultList();
    }
}
