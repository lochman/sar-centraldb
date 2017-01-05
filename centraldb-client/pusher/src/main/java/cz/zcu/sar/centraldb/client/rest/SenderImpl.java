package cz.zcu.sar.centraldb.client.rest;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
@Service
public class SenderImpl implements Sender {
    private static final Logger LOGGER = LoggerFactory.getLogger(SenderImpl.class);

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
            LOGGER.debug("LastBatchId: Received last batch id {}", result);
            return Objects.equals(result, batchId) ? MyResponse.SEND_NEW : MyResponse.SEND_OLD;
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            if (statusCode == HttpStatus.NOT_FOUND) {
                // if not found - client dont have a mark about sync -> send batch
                LOGGER.debug("LastBatchId: Server has no last batch id yet");
                return MyResponse.SEND_NEW;
            } else {
                // Other status code - wait
                LOGGER.warn("LastBatchId: error retrieving last batch id. Status code: {}", statusCode);
                return MyResponse.WAIT;
            }
        }
    }
    public void sendData(List<Person> persons,String batchId) {
        Batch batch = new Batch(clientId, normalizedPerson(persons), batchId);
        LOGGER.info("Sending batch {} of size {}", batch.getBatchId(), batch.getSize());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try{
        restTemplate.postForObject(uriData, batch, Batch.class);
        }catch (HttpClientErrorException e){
            LOGGER.error("Failed to send batch {} on URI {}", batch.getBatchId(), uriData);
            System.out.println(e.toString());
        }
    }


    public List<Person> fetchData(int size) {
        Batch batchRequest = new Batch();
        batchRequest.setClientId(clientId);
        batchRequest.setSize(size);
        LOGGER.info("Sending fetch request to server of size {}", size);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            Batch batch = restTemplate.postForObject(fetchData, batchRequest, Batch.class);
            if (batch == null) {
                return new ArrayList<>();
            } else {
                LOGGER.info("Received batch {} of size {}", batch.getBatchId(), batch.getSize());
                return normalizedPerson(batch.getPersons());
            }
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to receive batch from request {} on URI {}", batchRequest.getBatchId(), fetchData);
            return new ArrayList<>();
        }
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
