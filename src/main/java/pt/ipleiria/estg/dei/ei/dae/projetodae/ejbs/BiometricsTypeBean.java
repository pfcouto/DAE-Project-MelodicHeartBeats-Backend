package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Biometric;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class BiometricsTypeBean {
    @PersistenceContext
    EntityManager em;

    public BiometricsType create(String type, int max, int min, String unity, String administratorUsername){
        if((max<0 || min<0) || max<min){
            return null;
        }
        Administrator administrator=em.find(Administrator.class,administratorUsername);
        if(administrator!=null){
            BiometricsType biometricsType = new BiometricsType(type,max,min,unity,administrator);
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
        String queryString = "select b from Biometric b where b.biometricsType=:code";
        Query query=em.createQuery( queryString );
        query.setParameter("code",biometricsType);
        List<Biometric> biometrics=(List<Biometric>) query.getResultList();

        if(biometrics.isEmpty()){
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
            if(biometricsTypeDTO.getType()!=null){
                biometricsType.setType(biometricsTypeDTO.getType());
            }
            if(biometricsTypeDTO.getMax()!=-1){
                if(biometricsTypeDTO.getMax()>=biometricsType.getMin()){
                    biometricsType.setMax(biometricsTypeDTO.getMax());
                }else{
                    return false;
                }
            }
            if(biometricsTypeDTO.getMin()!=-1){
                if(biometricsType.getMax()>=biometricsTypeDTO.getMin()){
                    biometricsType.setMin(biometricsTypeDTO.getMin());
                }else{
                    return false;
                }
            }
            if(biometricsTypeDTO.getUnity()!=null){
                biometricsType.setUnity(biometricsTypeDTO.getUnity());
            }
        }else{
            return false;
        }
        em.merge(biometricsType);
        return true;
    }
}
