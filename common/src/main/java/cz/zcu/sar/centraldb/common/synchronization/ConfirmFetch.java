package cz.zcu.sar.centraldb.common.synchronization;

import java.sql.Timestamp;

/**
 * @author Marek Rasocha
 *         date 22.12.2016.
 */
public class ConfirmFetch {
    private Timestamp lastDate;
    private int size;
    private String clientId;

    public ConfirmFetch(String clientId, Timestamp lastDate, int size) {
        this.clientId = clientId;
        this.lastDate = lastDate;
        this.size = size;
    }

    public ConfirmFetch() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Timestamp getLastDate() {
        return lastDate;
    }

    public void setLastDate(Timestamp lastDate) {
        this.lastDate = lastDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
