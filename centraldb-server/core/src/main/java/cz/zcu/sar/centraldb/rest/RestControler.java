package cz.zcu.sar.centraldb.rest;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.core.SyncService;
import cz.zcu.sar.centraldb.persistence.domain.*;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;


/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
@RestController
public class RestControler {

    @Autowired
    private SyncService syncService;

    @Autowired
    private InstituteService instituteService;

    @PostMapping("/lastBatch")
    public ResponseEntity<String> getLastBatch(@RequestBody String instituteId) {
        Optional<Institute> institute;
        try {
            Long id = Long.parseLong(instituteId);
            institute = instituteService.findOne(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return institute.isPresent() ? new ResponseEntity<>(institute.get().getLastBatchId(), HttpStatus.OK) : new ResponseEntity<>("NOT FOUND",HttpStatus.NOT_FOUND);
    }

    @PostMapping("/data")
    public ResponseEntity<Batch> getData(@RequestBody() Batch batch) {
        syncService.pushRequest(batch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/data/fetch")
    public ResponseEntity<Batch> fetchData(@RequestBody() Batch batchRequest) {
        Batch batch = syncService.retrieveData(batchRequest.getClientId(), batchRequest.getSize());
        return new ResponseEntity<>(batch, HttpStatus.OK);
    }

    @PostMapping("/data/confirm")
    public ResponseEntity<ConfirmFetch> confirmFetch(@RequestBody() ConfirmFetch batch) {
        HttpStatus status = syncService.confirmBatch(batch) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(status);
    }

    /*
    public List<Person> run(){
        //Create person types
        Random random = new Random();
        List<PersonType> personTypes =initPersonType();
        List<AddressType> addressTypes = initAddressType();
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 110; i++) {
            Address address = new Address();
            Address address2 = new Address();
            Person person = new Person("Jmenooo" + i, "Prijmeniee" , "");
            if (i % 2 == 0) { person.setGender("m");
            } else { person.setGender("f"); }
            person.setModifiedBy("user1");
            person.setCompanyNumber(String.valueOf(i));
            person.setSocialNumber(String.valueOf(i));
            // generate modified time
            long curr = System.currentTimeMillis();
            long hour = 60*60*1000;
            int cislo = random.nextInt(7*24);        // 7 dni a 24 hod
            person.setModifiedTime(new Timestamp(curr-(hour*cislo)));

            if( i % 5 == 0) {
                person.setPersonType(personTypes.get(0));
                address.setAddressType(addressTypes.get(0));
                address2.setAddressType(addressTypes.get(0));
            }
            else{
                person.setPersonType(personTypes.get(1));
                address.setAddressType(addressTypes.get(1));
                address2.setAddressType(addressTypes.get(1));
            }
            person.setBirthDate(new Date());
            // personRepository.save(person);
            if(i%10 !=0){
                address.setStreet("Dlouhá" + i);
                address.setLand_registry_number(String.valueOf(i));
                address.setCity("Praha" + i);
                address.setModifiedBy("init");
                address.setPerson(person);

                address2.setStreet("Kratka" + i);
                address2.setLand_registry_number(String.valueOf(i));
                address2.setCity("Plisen" + i);
                address2.setModifiedBy("init");
                address2.setPerson(person);
                Set set = new HashSet<>();
                set.add(address);
                set.add(address2);
                person.setAddressWrappers(set);
            }
            persons.add(person);
        }
        return persons;
    }
    private cz.zcu.sar.centraldb.common.persistence.Person[] normalizedPerson(List<Person> persons) {
        cz.zcu.sar.centraldb.common.persistence.Person[] wrapper = new cz.zcu.sar.centraldb.common.persistence.Person[persons.size()];
        int i = 0;
        for(Person p : persons) {
//            cz.zcu.sar.centraldb.common.persistence.Person<cz.zcu.sar.centraldb.common.persistence.PersonType,cz.zcu.sar.centraldb.common.persistence.Address> w = p.getPersonWrapper();
            wrapper[i++] = p.getPersonWrapper();
        }
        return wrapper;
    }
    private List<AddressType> initAddressType() {
        //Create address types
        AddressType at1 = new AddressType();
        at1.setId((long) 3);
        at1.setDescription("Trvalá adresa");
        at1.setModifiedBy("init");
        AddressType at2 = new AddressType();
        at2.setId((long) 4);
        at2.setDescription("Přechodná adresa");
        at2.setModifiedBy("init");
        List<AddressType> list = new ArrayList<>();
        list.add(at1);
        list.add(at2);
        return list;
    }

    private List<PersonType> initPersonType() {
        PersonType t1 = new PersonType();
        t1.setId((long) 1);
        t1.setDescription("Fyzická osoba");
        t1.setModifiedBy("init");
        PersonType t2 = new PersonType();
        t2.setId((long) 2);
        t2.setDescription("Právnická osoba");
        t2.setModifiedBy("init");
        List<PersonType> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        return list;
    }
    */
}
