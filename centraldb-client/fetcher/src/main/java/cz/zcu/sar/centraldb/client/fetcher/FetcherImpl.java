package cz.zcu.sar.centraldb.client.fetcher;

import cz.zcu.sar.centraldb.client.Merger.Merger;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.rest.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class FetcherImpl implements Fetcher {
    @Autowired
    Sender sender;
    @Autowired
    Merger merger;
    @Override
    public void fetchData() {
        List<Person> persons = sender.fetchData();
        if (!persons.isEmpty()){
            merger.mergeData(persons);
        }

    }
}
