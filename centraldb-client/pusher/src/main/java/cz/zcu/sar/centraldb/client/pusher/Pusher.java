package cz.zcu.sar.centraldb.client.pusher;

/**
 * try to send data on the server
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface Pusher {
    /**
     * get flags from last sync.
     * 1) ask server from lastBatchID (SID) that was sync
     * 2) equals SID and client lastBatchID(LID). If result is:
     *      true: - create new batch and send it on the server.
     *            - update sync flags
     *      false: - send old batch that is in ram. Also we can reconstruct it from db by syncDAte flags
     *      server can response wait(some error state). After that, pusher do nothing
     *
     *
     */
    void pushData();
}
