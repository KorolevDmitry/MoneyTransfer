package moneyTransfer.dao;

import java.math.BigDecimal;

/**
 * Created by dkorolev on 4/15/2017.
 * Interface of simple Account Data Access Object
 * Each method should do atomic update in order to stay consistent
 */
public interface AccountDao {
    long createAccount();
    BigDecimal getAccountBalance(long id);
    void updateAccountBalance(long id, BigDecimal value);
    void updateAccountBalance(long id1, BigDecimal value1, long id2, BigDecimal value2);
}
