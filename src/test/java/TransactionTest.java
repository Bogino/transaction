import junit.framework.TestCase;
import sun.security.mscapi.PRNG;


public class TransactionTest extends TestCase {

    private final int NUMBERS_OF_ACCOUNTS = 400;
    private final int CORES_AMOUNT = 8;
    private final int FRAUD_LIMIT = 50000;
    private final int NUMBER_OF_OPERATIONS_FOR_THREAD = 1000;
    private final int MONEY_IN_ACCOUNT = 10000000;
    private int fromAccountNum = (int)Math.random()*400;
    private int toAccountNum = (int)Math.random()*400;


    @Override
    protected void setUp() throws Exception {

        Bank alfa = new Bank();


        for (int i = 1; i <= NUMBERS_OF_ACCOUNTS; i++){
            Account account = new Account();
            account.setAccNumber(String.valueOf(i));
            account.setMoney(MONEY_IN_ACCOUNT);
        }

    }
}
