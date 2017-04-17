package moneyTransfer.client;

import moneyTransfer.MTService;

import java.util.Scanner;

/**
 * Created by dkorolev on 4/17/2017.
 */
public class MTClient {
    private static final String BASE_ADDRESS = "http://0.0.0.0:9000/mtservice";

    public static void main(String[] args) {
        String baseAddress = args[0];
        MTService service = MTServiceFactory.getServiceInstance(baseAddress);
        Scanner scan = new Scanner(System.in);
        boolean running = true;
        while(running) {
            try {
                String line = scan.nextLine();
                String[] split = line.split(" ");
                if (split.length == 0) {
                    throw new IllegalStateException("Unexpected number of tokens");
                }
                switch (split[0]) {
                    case "create":
                        if (split.length != 1)
                            throw new IllegalStateException("Unexpected number of tokens");
                        System.out.println(service.createAccount());
                        break;
                    case "get":
                        if (split.length != 2)
                            throw new IllegalStateException("Unexpected number of tokens");
                        long accountId = Long.parseLong(split[1]);
                        System.out.println(service.getAccount(accountId));
                        break;
                    case "deposit":
                        if (split.length != 3)
                            throw new IllegalStateException("Unexpected number of tokens");
                        accountId = Long.parseLong(split[1]);
                        double value = Double.parseDouble(split[2]);
                        service.deposit(accountId, value);
                        System.out.println("Done");
                        break;
                    case "withdraw":
                        if (split.length != 3)
                            throw new IllegalStateException("Unexpected number of tokens");
                        accountId = Long.parseLong(split[1]);
                        value = Double.parseDouble(split[2]);
                        service.withdraw(accountId, value);
                        System.out.println("Done");
                        break;
                    case "transfer":
                        if (split.length != 4)
                            throw new IllegalStateException("Unexpected number of tokens");
                        long accountId1 = Long.parseLong(split[1]);
                        long accountId2 = Long.parseLong(split[2]);
                        value = Double.parseDouble(split[3]);
                        service.transfer(accountId1, accountId2, value);
                        System.out.println("Done");
                        break;
                    case "quit":
                        if (split.length != 1)
                            throw new IllegalStateException("Unexpected number of tokens");
                        running = false;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operation");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                printUsage();
            }
        }
    }

    private static void printUsage() {
        System.out.println("Available commands: create, get, deposit, withdraw, transfer, quit\n" +
                "create - create account and return it's id\n" +
                "get [accountId] - get account balance for accountId\n" +
                "deposit [accountId] [value] - deposit value on accountId\n" +
                "withdraw [accountId] [value] - withdraw value from accountId\n" +
                "transfer [accountId1] [accountId2] [value] - transfer value from accountId1 to accountId2\n" +
                "quit - shutdown client");
    }
}
