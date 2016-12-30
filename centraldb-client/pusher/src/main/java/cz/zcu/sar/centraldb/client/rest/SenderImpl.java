package cz.zcu.sar.centraldb.client.rest;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
// TODO: otestovat a dodelat
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

    public boolean sendLastBatchId(String batchId) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uriBatch, clientId, String.class);
        return result.equals(batchId);
    }
    public void sendData(List<Person> persons,String batchId) {
        Batch batchWrapper = new Batch(clientId, normalizedPerson(persons));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForObject(uriData, batchWrapper,Batch.class);
    }

    //TODO: send clientId and int size in ConfimFetch, instead of Batch
    public List<Person> fetchData() {
        Batch batchWrapper = new Batch();
        batchWrapper.setClientId(clientId);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Batch batch = restTemplate.postForObject(fetchData, batchWrapper, Batch.class);
        return batch != null ? normalizedPerson(batch.getPersons()) : new ArrayList<>();
    }



    public void confirmFetchData(Timestamp lastDate, int size) {
        ConfirmFetch param = new ConfirmFetch(clientId, lastDate,size);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForObject(confirmFetch, param, ConfirmFetch.class);
        return;
    }

    private cz.zcu.sar.centraldb.common.persistence.Person[] normalizedPerson(List<Person> persons) {
        cz.zcu.sar.centraldb.common.persistence.Person[] wrapper = new cz.zcu.sar.centraldb.common.persistence.Person[persons.size()];
        int i=0;
        for(Person p : persons){
            cz.zcu.sar.centraldb.common.persistence.Person w = p.getWraperPerson();
            wrapper[i++] = (w);
        }
        return wrapper;
    }
    private List<Person> normalizedPerson(cz.zcu.sar.centraldb.common.persistence.Person[] persons) {
        List<Person> normalized = new ArrayList<>();
        if (persons == null) return normalized;
        for(int i=0;i<persons.length;i++){
            normalized.add(new Person(persons[i]));
        }
        return normalized;
    }

}
