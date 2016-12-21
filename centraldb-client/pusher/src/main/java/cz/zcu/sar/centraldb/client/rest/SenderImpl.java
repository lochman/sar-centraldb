package cz.zcu.sar.centraldb.client.rest;




import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wrapper.BatchWrapper;

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
    public void sendData(List<Person> persons,String batchId){
        BatchWrapper batchWrapper = new BatchWrapper(clientId,normalizedPerson(persons));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForObject(uriData, batchWrapper, BatchWrapper.class);
    }

    private List<wrapper.Person> normalizedPerson(List<Person> persons) {
        List<wrapper.Person> wrapper = new ArrayList<>();
        for(Person p : persons){
            wrapper.Person w = p.getWraperPerson();
            wrapper.add(w);
        }
        return wrapper;
    }

    public List<Person> fetchData(){
        JSONObject params = new JSONObject();
        params.put("idClient",clientId);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(fetchData, String.class, params);
        //TODO : dodelat komunikaci
        return new ArrayList<>();
    }
    public void confirmFetchData(){
        JSONObject params = new JSONObject();
        params.put("idClient",clientId);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(confirmFetch, String.class, params);
    }
}
