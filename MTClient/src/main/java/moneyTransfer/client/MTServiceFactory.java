package moneyTransfer.client;

import moneyTransfer.MTService;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

/**
 * Created by dkorolev on 4/15/2017.
 */
public class MTServiceFactory {
    public static MTService getServiceInstance(String baseAddress) {
        return JAXRSClientFactory.create(baseAddress, MTService.class);
    }
}
