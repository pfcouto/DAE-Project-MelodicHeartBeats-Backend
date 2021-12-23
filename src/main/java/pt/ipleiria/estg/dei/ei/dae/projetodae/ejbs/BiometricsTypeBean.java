package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BiometricsTypeBean {
    @PersistenceContext
    EntityManager em;

    public BiometricsType create(String name,String description, int valueMax, int valueMin, String unity, String administratorUsername){
        if((valueMax<0 || valueMin<0) || valueMax<valueMin){
            return null;
        }
        System.out.println(administratorUsername);
        Administrator administrator=em.find(Administrator.class,administratorUsername);
        if(administrator!=null){
            BiometricsType biometricsType = new BiometricsType(name,description,valueMax,valueMin,unity,administrator);
            em.persist(biometricsType);
            administrator.addBiometricsType(biometricsType);
            em.merge(administrator);
            return biometricsType;
        }
        return null;
    }

    public List<BiometricsType> getAllBiometricsTypes() {
        return (List<BiometricsType>) em.createNamedQuery("getAllBiometricsTypes").getResultList();
    }

    public BiometricsType find(int code) {
        return em.find(BiometricsType.class,code);
    }

    public void delete(BiometricsType biometricsType) {
        if (!em.contains(biometricsType)) {
            biometricsType = em.merge(biometricsType);
        }
        biometricsType.getAdministrator().removeBiometricType(biometricsType);
        em.persist(biometricsType.getAdministrator());

        //get a list of Biometrics by Type.
        String queryString = "select b from Observation b where b.biometricsType=:code";
        Query query=em.createQuery( queryString );
        query.setParameter("code",biometricsType);
        List<Observation> observations =(List<Observation>) query.getResultList();

        if(observations.isEmpty()){
            //hard Delete
            em.remove(biometricsType);
        }else{
            //soft delete
            biometricsType.setDeleted(!biometricsType.isDeleted());
            em.persist(biometricsType);
        }
    }

    public boolean update(int code, BiometricsTypeDTO biometricsTypeDTO) {
        BiometricsType biometricsType=em.find(BiometricsType.class,code);
        if(biometricsType!=null){
            if(biometricsTypeDTO.getName()!=null){
                biometricsType.setName(biometricsTypeDTO.getName());
            }
            if(biometricsTypeDTO.getValueMax()!=-1){
                if(biometricsTypeDTO.getValueMax()>=biometricsType.getValueMin()){
                    biometricsType.setValueMax(biometricsTypeDTO.getValueMax());
                }else{
                    return false;
                }
            }
            if(biometricsTypeDTO.getValueMin()!=-1){
                if(biometricsType.getValueMax()>=biometricsTypeDTO.getValueMin()){
                    biometricsType.setValueMin(biometricsTypeDTO.getValueMin());
                }else{
                    return false;
                }
            }
            if(biometricsTypeDTO.getUnity()!=null){
                biometricsType.setUnity(biometricsTypeDTO.getUnity());
            }
            if(biometricsTypeDTO.getDescription().length()<255){
                biometricsType.setDescription(biometricsTypeDTO.getDescription());
            }
        }else{
            return false;
        }
        em.merge(biometricsType);
        return true;
    }

    public List<BiometricsType> getAllBiometricsTypesNonDeleted() {
        return (List<BiometricsType>) em.createNamedQuery("getAllBiometricsTypesNonDeleted").getResultList();
    }
}
