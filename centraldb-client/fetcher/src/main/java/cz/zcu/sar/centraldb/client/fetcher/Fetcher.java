package cz.zcu.sar.centraldb.client.fetcher;


/**
 * Fetcher
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface Fetcher {
    /**
     * Try to fetch data
     * Send a request to the server.
     * If data is in response - merge data
     * Otherwise do nothing
     */
    void fetchData();
}
