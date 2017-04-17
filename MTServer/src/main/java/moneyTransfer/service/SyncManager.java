package moneyTransfer.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class SyncManager {
    private final ConcurrentHashMap<Long, Object> locks;

    public SyncManager() {
        locks = new ConcurrentHashMap<>();
    }

    public Object getLock(long id) {
        Object temp = new Object();
        Object lock = locks.putIfAbsent(id, temp);
        return lock == null ? temp : lock;
    }
}
