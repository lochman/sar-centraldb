package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
@Service
public class BatchCreatorImpl implements BatchCreator{
    private static final long STEP = 60*60*1000;      // hour
    private static final int SIZE_BUFFER =100;
    private Timestamp startDate;
    private Timestamp endDate;

    @Autowired
    PersonRepository personRepository;

    @Override
    public List<Person> createBatch(Timestamp start) {
        List<Person> persons = new ArrayList<>();
        startDate = new Timestamp(start.getTime());
        Timestamp startDate = start;
        endDate = new Timestamp(start.getTime()+STEP);
        while (persons.size()<SIZE_BUFFER){
            if (endDate.getTime()>=System.currentTimeMillis()){
                persons.addAll(createBatchToCurrentTime(startDate));
                break;
            }
            persons.addAll(getDataByDate(startDate,endDate));
            startDate= new Timestamp(endDate.getTime());
            endDate = new Timestamp(endDate.getTime()+STEP);
        }
        return persons;
    }

    public List<Person> createBatch(Timestamp start,Timestamp end){
        startDate = start;
        endDate = end;
        return (getDataByDate(startDate,endDate));
    }

    public List<Person> createBatchToCurrentTime(Timestamp start) {
        startDate = start;
        endDate = new Timestamp(System.currentTimeMillis());
        return  getDataByDate(startDate,endDate);
    }


    private List<Person> getDataByDate(Timestamp startDate, Timestamp endDate) {
        List<Person> o = personRepository.findByDate(startDate, endDate);
        return o!=null ? o : new ArrayList<>();
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }
}