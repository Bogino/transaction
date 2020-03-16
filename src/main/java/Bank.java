import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bank {
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();
    private final int FRAUD_LIMIT = 50000;


    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) {

        synchronized (accounts.get(fromAccountNum)) {
            synchronized (accounts.get(toAccountNum)) {
                if (amount > FRAUD_LIMIT) {
                    try {
                        if (isFraud(fromAccountNum, toAccountNum, amount)) {
                            System.out.println("Счета заблокированы");
                        } else {
                            accounts.get(fromAccountNum).setMoney(getBalance(fromAccountNum) - amount);
                            accounts.get(toAccountNum).setMoney(getBalance(toAccountNum) + amount);
                        }
                    } catch(InterruptedException e){
                            e.printStackTrace();
                    } catch(IllegalArgumentException e){
                            System.out.println("На счете " + fromAccountNum + " недостаточно денег, попробуйте позже");
                        }
                    }
                else{
                        try {
                            accounts.get(fromAccountNum).setMoney(getBalance(fromAccountNum) - amount);
                            accounts.get(toAccountNum).setMoney(getBalance(toAccountNum) + amount);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("На счете " + fromAccountNum + " недостаточно денег, попробуйте позже");
                        }
                    }
                }
            }
        }


    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum)
    {
        return accounts.get(accountNum).getMoney();
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Account... clients) {
        for (Account client : clients){
            accounts.put(client.getAccNumber(), client);
        }
    }
    public void setAccounts(Account account){
        accounts.put(account.getAccNumber(), account);
    }
}
