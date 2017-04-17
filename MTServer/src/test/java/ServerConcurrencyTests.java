import moneyTransfer.MTService;
import moneyTransfer.MTServiceServer;
import moneyTransfer.dao.AccountDaoInMemoryImpl;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class ServerConcurrencyTests {
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
    public void Transfer_TwoAccounts_Done() throws InterruptedException {
        double deposit = 1000;
        long account1Id = mtService.createAccount();
        mtService.deposit(account1Id, deposit);
        long account2Id = mtService.createAccount();
        mtService.deposit(account2Id, deposit);
        final AtomicInteger errors = new AtomicInteger(0);
        int attempts = 100;

        Thread thread1 = new Thread(() -> {
            try {
                for (int i = 0; i < attempts; i++) {
                    mtService.transfer(account1Id, account2Id, 1);
                }
            } catch (Exception e) {
                errors.incrementAndGet();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                for (int i = 0; i < attempts; i++) {
                    mtService.transfer(account2Id, account1Id, 1);
                }
            } catch (Exception e) {
                errors.incrementAndGet();
            }
        });
        Thread thread3 = new Thread(() -> {
            try {
                for (int i = 0; i < attempts; i++) {
                    if (i % 2 == 0) {
                        mtService.transfer(account1Id, account2Id, 1);
                    } else {
                        mtService.transfer(account2Id, account1Id, 1);
                    }
                }
            } catch (Exception e) {
                errors.incrementAndGet();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        double balance1 = mtService.getAccount(account1Id);
        double balance2 = mtService.getAccount(account2Id);

        assertEquals(0, errors.intValue());
        assertEquals(deposit, balance1, EPS);
        assertEquals(deposit, balance2, EPS);
    }
}
