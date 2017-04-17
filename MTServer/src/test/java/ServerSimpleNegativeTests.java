import moneyTransfer.MTService;
import moneyTransfer.MTServiceServer;
import moneyTransfer.dao.AccountDaoInMemoryImpl;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class ServerSimpleNegativeTests {
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
    public void Withdraw_NotEnoughMoney_Fail() {
        long accountId = mtService.createAccount();
        double withdraw = 100;
        boolean error = false;

        try {
            mtService.withdraw(accountId, withdraw);
        } catch (Exception e) {
            error = true;
        }
        double balance = mtService.getAccount(accountId);

        assertTrue(error);
        assertEquals(0, balance, EPS);
    }

    @Test
    public void Transfer_NotEnoughMoney_Fail() {
        long account1Id = mtService.createAccount();
        long account2Id = mtService.createAccount();
        double transfer = 100;
        boolean error = false;

        try {
            mtService.transfer(account1Id, account2Id, transfer);
        } catch (Exception e) {
            error = true;
        }

        double balance1 = mtService.getAccount(account1Id);
        double balance2 = mtService.getAccount(account2Id);

        assertTrue(error);
        assertEquals(0, balance1, EPS);
        assertEquals(0, balance2, EPS);
    }
}
