package cz.zcu.sar.centraldb.client.rest;




import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
// TODO: otestovat a dodelat
@Service
public class SenderImpl implements Sender {
    final String uri = "http://localhost:8080/lastBatch";
    final String uriData = "http://localhost:8080/data";
    final String fetchData = "http://localhost:8080/data/fetch";
    final String clientId="client1";

    public boolean sendLastBatchId(String batchId) {
        Map<String, String> params = new HashMap();
        params.put("batchId", batchId);
        params.put("idClient", clientId);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class, params);
        return result.equals(batchId);
    }
    public void sendData(List<Person> persons,String batchId){
        JSONObject params = new JSONObject();
        params.put("idClient",clientId);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(persons);
        params.put("data",jsonArray);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uriData, String.class, params);
    }
    public List<Person> fetchData(){
        JSONObject params = new JSONObject();
        params.put("idClient",clientId);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(fetchData, String.class, params);
        //TODO : dodelat komunikaci
        return new ArrayList<>();
    }
}
