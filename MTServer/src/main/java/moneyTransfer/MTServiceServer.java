package moneyTransfer;

import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.AccountDaoInMemoryImpl;
import moneyTransfer.service.MTServiceImpl;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class MTServiceServer {
    private static final String BASE_ADDRESS = "http://0.0.0.0:9000/mtservice";
    private final Server server;

    public MTServiceServer(String baseAddress, AccountDao accountDao) {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setResourceClasses(MTServiceImpl.class);
        factory.setResourceProvider(MTServiceImpl.class, new SingletonResourceProvider(new MTServiceImpl(accountDao)));
        factory.setAddress(baseAddress);
        server = factory.create();
    }

    public void shutdown() {
        server.stop();
    }

    public static void main(String[] args) {
        new MTServiceServer(BASE_ADDRESS, new AccountDaoInMemoryImpl());
        System.out.println("Server started on " + BASE_ADDRESS);
        try {
            while (true) {
                System.out.println("Server is running...");
                Thread.sleep(5 * 60 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
        }
    }
}
