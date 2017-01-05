package cz.zcu.sar.centraldb.merger;



import cz.zcu.sar.centraldb.persistence.domain.Person;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface Merger {
    /**
     * merge data
     * @param temporal temporal person
     * @param persist persist person
     * @return merge ok
     */
    Person mergeData(Person temporal, Person persist);
}
