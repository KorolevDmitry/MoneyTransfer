import moneyTransfer.MTService;
import moneyTransfer.MTServiceServer;
import moneyTransfer.dao.AccountDaoInMemoryImpl;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class ServerSimplePositiveTests {
    private static final String BASE_ADDRESS = "http://0.0.0.0:9000/mtservice";
    private static final double EPS = 0.000001;
    private MTServiceServer server;
    private MTService mtService;

    @Before
    public void before(){
        server = new MTServiceServer(BASE_ADDRESS, new AccountDaoInMemoryImpl());
        mtService = JAXRSClientFactory.create(BASE_ADDRESS, MTService.class);
    }

    @After
    public void after(){
        server.shutdown();
    }

    @Test
    public void CreateAccount_Simple_AccountExists() {

        long accountId = mtService.createAccount();
        double balance = mtService.getAccount(accountId);
        server.shutdown();

        assertEquals(0, balance, 0.00001);
    }

    @Test
    public void Deposit_Simple_DepositDone() {
        long accountId = mtService.createAccount();
        double deposit = 100;

        mtService.deposit(accountId, deposit);
        double balance = mtService.getAccount(accountId);

        assertEquals(deposit, balance, EPS);
    }

    @Test
    public void Withdraw_Simple_WithdrawDone() {
        long accountId = mtService.createAccount();
        double deposit = 100;
        mtService.deposit(accountId, deposit);
        double withdraw = 100;

        mtService.withdraw(accountId, withdraw);
        double balance = mtService.getAccount(accountId);

        assertEquals(deposit - withdraw, balance, EPS);
    }

    @Test
    public void Transfer_Simple_TransferDone() {
        long account1Id = mtService.createAccount();
        long account2Id = mtService.createAccount();
        double deposit = 100;
        mtService.deposit(account1Id, deposit);
        double transfer = 100;
        mtService.transfer(account1Id, account2Id, transfer);

        double balance1 = mtService.getAccount(account1Id);
        double balance2 = mtService.getAccount(account2Id);

        assertEquals(deposit - transfer, balance1, EPS);
        assertEquals(transfer, balance2, EPS);
    }
}
