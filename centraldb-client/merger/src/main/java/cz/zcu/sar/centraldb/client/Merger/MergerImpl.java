package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.Synchronization;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.SynchronizationRepository;
import cz.zcu.sar.centraldb.client.persistence.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class MergerImpl implements Merger {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    SynchronizationRepository synchronizationRepository;
    @Autowired
    BaseService baseService;

    @Override
    public boolean mergeData(List<Person> persons){
        for (Person p : persons){
            Person save = null;
            if (p.getId()!=null){
                save = personRepository.findOne(p.getId());
            }
            if(save!=null){
                if(save.getModifiedTime().after(p.getModifiedTime())){
                    continue;
                }
            }else{
                p = (Person)baseService.setModifyBy(p,true);
                personRepository.save(p);
            }
        }
        return true;
    }
}
