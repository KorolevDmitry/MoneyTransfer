package moneyTransfer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class AccountDaoInMemoryImpl implements AccountDao {
    private final Map<Long, BigDecimal> accounts;
    private final AtomicInteger accountIdGen;

    public AccountDaoInMemoryImpl() {
        this.accounts = new ConcurrentHashMap<>();
        this.accountIdGen = new AtomicInteger(0);
    }

    @Override
    public long createAccount() {
        BigDecimal value = new BigDecimal(0);
        long accountId = accountIdGen.getAndIncrement();
        accounts.put(accountId, value);
        return accountId;
    }

    @Override
    public BigDecimal getAccountBalance(long id) {
        return accounts.get(id);
    }

    @Override
    public void updateAccountBalance(long id, BigDecimal value) {
        accounts.put(id, value);
    }

    @Override
    public void updateAccountBalance(long id1, BigDecimal value1, long id2, BigDecimal value2) {
        accounts.put(id1, value1);
        accounts.put(id2, value2);
    }
}
