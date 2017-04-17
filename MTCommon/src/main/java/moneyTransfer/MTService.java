package moneyTransfer;

import javax.ws.rs.*;

/**
 * Created by dkorolev on 4/15/2017.
 */
public interface MTService {
    @PUT
    @Path("/account")
    long createAccount();

    @GET
    @Path("/account")
    double getAccount(@QueryParam("id") long accountId);

    @POST
    @Path("/deposit")
    void deposit(@QueryParam("id") long accountId, @QueryParam("value") double value);

    @POST
    @Path("/withdraw")
    void withdraw(@QueryParam("id") long accountId, @QueryParam("value") double value);

    @POST
    @Path("/transfer")
    void transfer(@QueryParam("fromId") long fromAccountId, @QueryParam("toId") long toAccountId,
                  @QueryParam("value") double value);
}
