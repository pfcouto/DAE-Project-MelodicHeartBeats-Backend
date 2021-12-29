package pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.QualitativeValuesDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Scanner;

@Stateless
public class BiometricsTypeBean {
    @PersistenceContext
    EntityManager em;

    public BiometricsType create(String name,String description, int valueMax, int valueMin, String unity, String administratorUsername,List<QualitativeValuesDTO> qualitativeValues) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
        if(valueMax<0){
            throw new MyIllegalArgumentException("The Max Value must be bigger than or equal to 0!");
        }
        if(valueMin<0){
            throw new MyIllegalArgumentException("The Min Value must be bigger than or equal to 0!");
        }
        if(valueMax<valueMin){
            throw new MyIllegalArgumentException("The Max Value must be bigger than the Min value");
        }
        Administrator administrator=em.find(Administrator.class,administratorUsername);
        if(administrator!=null){
            try {
                BiometricsType biometricsType = new BiometricsType(name,description,valueMax,valueMin,unity,administrator);
                for (QualitativeValuesDTO o : qualitativeValues) {
                    if(o.value>valueMax || o.value<valueMin){
                        throw new MyIllegalArgumentException("All Quantitatives values should be between "+ valueMax +" and " + valueMin + "!");
                    }
                    biometricsType.getListOfQualitativeValues().put(o.value,o.meaning);
                }
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
        //get a list of Observations by Type.
        //String queryString = "select b from Observation b where b.biometricsType=:code";
        Query query=em.createNamedQuery("getAllObservationsByType");
        query.setParameter("code",biometricsType);
        List<Observation> observations =(List<Observation>) query.getResultList();
    try{
        if(observations.isEmpty()){
            //hard Delete
            try{
                if (!em.contains(biometricsType)) {
                    biometricsType = em.merge(biometricsType);
                }
                biometricsType.getAdministrator().removeBiometricType(biometricsType);
                em.persist(biometricsType.getAdministrator());
                em.remove(biometricsType);
            }catch(PersistenceException exception){
                throw new PersistenceException(exception);
            }
        }else{
            //soft deleted
           if(biometricsType.getDeleted_at()!="null"){
               biometricsType.setDeleted_at("null");
           }else{
               Calendar calendar = Calendar.getInstance();
               SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
               biometricsType.setDeleted_at(formatter.format(calendar.getTime()));
           }

            em.merge(biometricsType);
        }
    } catch (PersistenceException exception){
        throw new PersistenceException(exception);
    }
    }

    public BiometricsType update(int code, BiometricsTypeDTO biometricsTypeDTO) throws MyEntityNotFoundException,MyIllegalArgumentException {
        BiometricsType biometricsType=em.find(BiometricsType.class,code);
        if(biometricsType!=null){
            em.lock(biometricsType, LockModeType.OPTIMISTIC);
            if(biometricsTypeDTO.getName()!=""){
                biometricsType.setName(biometricsTypeDTO.getName());
            }else{
                throw new MyIllegalArgumentException("The Name should not be empty");
            }
            if(biometricsTypeDTO.getValueMax()!=-1){
                if(biometricsTypeDTO.getValueMax()<0){
                    throw new MyIllegalArgumentException("The Max Value must be bigger than or equal to 0!");
                }
                if(biometricsTypeDTO.getValueMax()>biometricsType.getValueMin()){
                    biometricsType.setValueMax(biometricsTypeDTO.getValueMax());
                }else{
                    throw new MyIllegalArgumentException("The Max Value must be bigger than the Min value");
                }
            }else{
                throw new MyIllegalArgumentException("The Max Value should not be empty!");
            }
            if(biometricsTypeDTO.getValueMin()!=-1){
                if(biometricsTypeDTO.getValueMin()<0){
                    throw new MyIllegalArgumentException("The Min Value must be bigger than or equal to 0!");
                }
                if(biometricsType.getValueMax()>biometricsTypeDTO.getValueMin()){
                    biometricsType.setValueMin(biometricsTypeDTO.getValueMin());
                }else{
                    throw new MyIllegalArgumentException("The Max Value must be bigger than the Min value");
                }
            }
            else{
                throw new MyIllegalArgumentException("The Min Value should not be empty!");
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
            Map<Integer,String> listOfQualitativeValues=new HashMap<>();
            for (QualitativeValuesDTO qualitative : biometricsTypeDTO.getQualitatives()) {
                if(biometricsType.getListOfQualitativeValues().containsKey(qualitative.value)){
                    throw new MyIllegalArgumentException("The Quantitive value should be unique.");
                }
                if(qualitative.value>biometricsTypeDTO.getValueMax() || qualitative.value<biometricsTypeDTO.getValueMin()){
                    throw new MyIllegalArgumentException("All Quantitatives values should be between "+ biometricsTypeDTO.getValueMax()+" and " + biometricsTypeDTO.getValueMin() + "!");
                }
                listOfQualitativeValues.put(qualitative.value,qualitative.meaning);
            }
            biometricsType.setListOfQualitativeValues(listOfQualitativeValues);
            em.merge(biometricsType);
        }else{
             throw new MyEntityNotFoundException("The Biometric Type has not found");
        }
        return biometricsType;
    }

    public void uploadCsvFile(){

    }


    public void readCsvFile(String filepath) throws FileNotFoundException, MyConstraintViolationException, MyEntityNotFoundException, MyIllegalArgumentException {
        try {
            File file=new File(filepath);
            /*FileWriter myWriter = new FileWriter(file);
            myWriter.write("waldomiro,grande,50,1,w,admin,60-teste:20-teste2:30-teste3");
            myWriter.write(System.getProperty( "line.separator" ));
            myWriter.write("Pedro,grande Pedro,50,1,w,admin,10-teste:20-teste2:30-teste3,teste");
            myWriter.write(System.getProperty( "line.separator" ));
            myWriter.write("Pedro2,grande Pedro2,50,1,w,admin,10-teste:20-teste2:30-teste3");
            myWriter.close();*/
            Scanner sc = new Scanner(file);
            //sets the delimiter pattern
            int line_number=1;
            while (sc.hasNextLine())  //returns a boolean value
            {
                String[] fields=sc.nextLine().split(",");
                if(fields.length==7){
                    List<QualitativeValuesDTO> qualitativeValuesDTOList = new LinkedList<>();
                   String[] ArrayParts=fields[6].split(":");
                    for (String s : ArrayParts) {
                        String[] Values=s.split("-");
                        QualitativeValuesDTO qualitativeValuesDTO=new QualitativeValuesDTO();
                        qualitativeValuesDTO.setValue(Integer.parseInt(Values[0]));
                        qualitativeValuesDTO.setMeaning(Values[1]);
                        qualitativeValuesDTOList.add(qualitativeValuesDTO);
                    }
                    try{
                        create(fields[0],fields[1],Integer.parseInt(fields[2]),Integer.parseInt(fields[3]),fields[4],fields[5],qualitativeValuesDTOList);
                    }catch (Exception e){
                        System.out.println("ERROR "+line_number+": "+e.getMessage());
                    }
                }else {
                    System.out.println("A linha: "+ line_number + " nao apresenta o formato correto.");
                }
                line_number++;
                //System.out.print(parts[6]);//find and returns the next complete token from this scanner
            }
            sc.close();  //closes the scanner
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<BiometricsType> getAllBiometricsTypesNonDeleted() {
        return (List<BiometricsType>) em.createNamedQuery("getAllBiometricsTypesNonDeleted").getResultList();
    }

//    public void assignBiometricTypeToAdministrator(String administrator_useranme, int biometricType_code) throws MyEntityNotFoundException {
//        Administrator administrator = em.find(Administrator.class, administrator_useranme);
//        if (administrator == null) {
//            throw new MyEntityNotFoundException("Administrator " + administrator_useranme + " does not exist");
//        }
//
//        BiometricsType biometricsType = find(biometricType_code);
//        if (biometricsType == null) {
//            throw new MyEntityNotFoundException("BiometricsType " + biometricType_code + " does not exist");
//        }
//
//        administrator.addBiometricsType(biometricsType);
//        biometricsType.setAdministrator(administrator);
//    }
}
