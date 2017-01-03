package cz.zcu.sar.centraldb.client.rest;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
@Service
public class SenderImpl implements Sender {
    @Value("${uri.lastBatch}")
    String uriBatch;
    @Value("${uri.getData}")
    String uriData;
    @Value("${uri.fetch}")
    String fetchData;
    @Value("${uri.fetch.confirm}")
    String confirmFetch;
    @Value("${client.id}")
    String clientId;

    public MyResponse sendLastBatchId(String batchId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.postForObject(uriBatch, clientId, String.class);
            return result.equals(batchId) ? MyResponse.SEND_NEW : MyResponse.SEND_OLD;
        }catch (HttpClientErrorException e){
            HttpStatus statusCode = e.getStatusCode();
            // if not found - client dont have a mark about sync -> send batch
            // Other status code - wait
            return statusCode == HttpStatus.NOT_FOUND ? MyResponse.SEND_NEW : MyResponse.WAIT;
        }
    }
    public void sendData(List<Person> persons,String batchId) {
        Batch batchWrapper = new Batch(clientId, normalizedPerson(persons));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try{
        restTemplate.postForObject(uriData, batchWrapper,Batch.class);
        }catch (HttpClientErrorException e){
            System.out.println(e.toString());
        }
    }


    public List<Person> fetchData(int size) {
        Batch batchWrapper = new Batch();
        batchWrapper.setClientId(clientId);
        batchWrapper.setSize(size);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Batch batch = restTemplate.postForObject(fetchData, batchWrapper, Batch.class);
        try {
            return batch != null ? normalizedPerson(batch.getPersons()) : new ArrayList<>();
        }catch (HttpClientErrorException e){return new ArrayList<>();}
    }



    public void confirmFetchData(Timestamp lastDate, int size) {
        ConfirmFetch param = new ConfirmFetch(clientId, lastDate,size);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            restTemplate.postForObject(confirmFetch, param, ConfirmFetch.class);
        }catch (HttpClientErrorException e){
            System.out.println(e.toString());
        }
    }

    private PersonWrapper[] normalizedPerson(List<Person> persons) {
        PersonWrapper[] wrapper = new PersonWrapper[persons.size()];
        int i = 0;
        for(Person p : persons){
            wrapper[i++] = p.getWraperPerson();
        }
        return wrapper;
    }


    private List<Person> normalizedPerson(PersonWrapper[] persons) {
        List<Person> normalized = new ArrayList<>();
        if (persons == null) return normalized;
        for (PersonWrapper person : persons) {
            Person p = new Person(person);
            p.setPersonType(new PersonType(p.getPersonType()));
            normalized.add(p);
        }
        return normalized;
    }

}
