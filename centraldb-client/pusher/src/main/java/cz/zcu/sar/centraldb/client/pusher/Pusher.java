package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;


import java.sql.Timestamp;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface Pusher {
    public void pushData();
}
