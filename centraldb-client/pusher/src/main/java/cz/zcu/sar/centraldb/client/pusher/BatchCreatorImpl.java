package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.services.PersonService;
import cz.zcu.sar.centraldb.client.persistence.services.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
@Service
public class BatchCreatorImpl implements BatchCreator{
    @Value("${batchCreator.step}")
    private long STEP;      // hour
    @Value("${batchCreator.bufferSize}")
    private int SIZE_BUFFER;
    private Timestamp startDate;
    private Timestamp endDate;

    @Autowired
    private PersonService personService;
    @Autowired
    private UtilService utilService;

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
        //List<Person> personCompleted = fillLazyAttribute(persons);
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
        List<Person> o = personService.findPersonByDate(startDate, endDate);
        if (o==null) o = new ArrayList<>();
        return o.stream().map(utilService::fillLazyAttribute).collect(Collectors.toList());
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }
}
