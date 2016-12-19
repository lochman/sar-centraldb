package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Matej Lochman on 16.12.2016.
 */

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BaseService baseService;
    @Autowired
    private Pusher pusher;

    public void run(String... strings) throws Exception {
        pusher.pushData();
    }

   private void initTestData(){
       Random random = new Random();
       for (int i=0;i<1000;i++){
           Person person = new Person();
           baseService.setDefaultValue(person,true);
           person.setName("Karel");
           person.setBirthDate(new Date());
           person.setModifiedBy("user1");
           long curr = System.currentTimeMillis();
           long hour = 60*60*1000;
           int cislo = random.nextInt(7*24);        // 7 dni a 24 hod
           person.setModifiedTime(new Timestamp(curr-(hour*cislo)));
           personRepository.save(person);
       }
    }

}
