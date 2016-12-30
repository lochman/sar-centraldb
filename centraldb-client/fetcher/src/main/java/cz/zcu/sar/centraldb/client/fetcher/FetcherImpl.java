package cz.zcu.sar.centraldb.client.fetcher;

import cz.zcu.sar.centraldb.client.Merger.Merger;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.rest.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class FetcherImpl implements Fetcher {
    @Value("${fetch.batchSize}")
    private int sizeBatch;
    @Autowired
    Sender sender;
    @Autowired
    Merger merger;
    @Override
    public void fetchData() {
        List<Person> persons = sender.fetchData(sizeBatch);
        if (!persons.isEmpty()){
            merger.mergeData(persons);
            Timestamp maxDate = new Timestamp(0);
            for (Person p : persons){
                if(maxDate.after(p.getModifiedTime())) maxDate=p.getModifiedTime();
            }
            sender.confirmFetchData(maxDate, persons.size());
        }

    }
}
