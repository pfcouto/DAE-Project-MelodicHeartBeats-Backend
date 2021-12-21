package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BiometricsTypeBean {
    @PersistenceContext
    EntityManager em;

    public BiometricsType create(String type, int max, int min, String unity, String administratorUsername){
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
        em.remove(biometricsType);
    }
}
