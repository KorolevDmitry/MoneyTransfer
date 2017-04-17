package moneyTransfer.service;

import moneyTransfer.MTService;
import moneyTransfer.dao.AccountDao;

import java.math.BigDecimal;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class MTServiceImpl implements MTService {
    private static final double EPS = 0.000001;
    private final SyncManager syncManager = new SyncManager();
    private final AccountDao accountDao;

    public MTServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long createAccount() {
        return accountDao.createAccount();
    }

    @Override
    public double getAccount(long accountId) {
        BigDecimal balance = accountDao.getAccountBalance(accountId);
        if (balance == null) {
            throw new IllegalStateException("Unknown accountId: " + accountId);
        }
        return balance.doubleValue();
    }

    @Override
    public void deposit(long accountId, double value) {
        if (value <= EPS) {
            throw new IllegalStateException("Attempt to deposit not positive amount: " + value);
        }
        BigDecimal valueDecimal = BigDecimal.valueOf(value);

        Object lock = syncManager.getLock(accountId);
        synchronized (lock) {
            BigDecimal balance = accountDao.getAccountBalance(accountId);
            if (balance == null) {
                throw new IllegalStateException("Unknown accountId: " + accountId);
            }
            accountDao.updateAccountBalance(accountId, balance.add(valueDecimal));
        }
    }

    @Override
    public void withdraw(long accountId, double value) {
        if (value <= EPS) {
            throw new IllegalStateException("Attempt to deposit not positive amount: " + value);
        }
        BigDecimal valueDecimal = BigDecimal.valueOf(value);

        Object lock = syncManager.getLock(accountId);
        synchronized (lock) {
            BigDecimal balance = accountDao.getAccountBalance(accountId);
            if (balance == null) {
                throw new IllegalStateException("Unknown accountId: " + accountId);
            }
            if (balance.compareTo(valueDecimal) < 0) {
                throw new IllegalStateException("Insufficient money: " + balance + ", asked: " + value);
            }

            accountDao.updateAccountBalance(accountId, balance.subtract(valueDecimal));
        }
    }

    @Override
    public void transfer(long fromAccountId, long toAccountId, double value) {
        if (value <= EPS) {
            throw new IllegalStateException("Attempt to transfer not positive amount: " + value);
        }
        BigDecimal valueDecimal = BigDecimal.valueOf(value);

        Object lock1 = syncManager.getLock(fromAccountId < toAccountId ? fromAccountId : toAccountId);
        Object lock2 = syncManager.getLock(fromAccountId > toAccountId ? fromAccountId : toAccountId);

        synchronized (lock1) {
            synchronized (lock2) {
                BigDecimal fromBalance = accountDao.getAccountBalance(fromAccountId);
                if (fromBalance == null) {
                    throw new IllegalStateException("Unknown fromAccountId: " + fromAccountId);
                }
                if (fromBalance.compareTo(valueDecimal) < 0) {
                    throw new IllegalStateException("Insufficient money: " + fromBalance + ", asked: " + value);
                }

                BigDecimal toBalance = accountDao.getAccountBalance(toAccountId);
                if (toBalance == null) {
                    throw new IllegalStateException("Unknown toAccountId: " + toAccountId);
                }
                accountDao.updateAccountBalance(
                        fromAccountId, fromBalance.subtract(valueDecimal),
                        toAccountId, toBalance.add(valueDecimal));
            }
        }
    }
}
