package cz.zcu.sar.centraldb.persistence.specification;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Matej Lochman on 21.12.16.
 */

public class PersonSpecifications {
    public static Specification<Person> hasProperties(Map<String, String> properties) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (root.get(entry.getKey()) != null) {
                    predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
                }
            }
            return cb.and(predicates.toArray(new Predicate[] {}));
        };
    }

    public static Specification<Person> isNewer(String property, Timestamp value) {
        return (root, query, cb) -> cb.greaterThan(root.<Timestamp>get(property), value);
    }
}
