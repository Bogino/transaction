import junit.framework.TestCase;

import java.util.Random;
import java.util.concurrent.Semaphore;


public class TransactionTest extends TestCase {

    private final int NUMBERS_OF_ACCOUNTS = 400;
    private final int CORES_AMOUNT = 8;
    private final int FRAUD_LIMIT = 50000;
    private final int NUMBER_OF_OPERATIONS_FOR_THREAD = 1000;
    private final int MONEY_IN_ACCOUNT = 10000000;
    Random random = new Random();
    private Bank alfa;
    private Bank sber;
    private Account vaska;
    private Account vitka;
    private Account gena;


    @Override
    protected void setUp() throws Exception {

        Semaphore sem = new Semaphore(1, true);
        alfa = new Bank();
        for (int i = 1; i <= NUMBERS_OF_ACCOUNTS; i++){
            alfa.setAccounts(new Account(String.valueOf(i), MONEY_IN_ACCOUNT));
        }
        sber = new Bank();
        for (int i = 1; i <= NUMBERS_OF_ACCOUNTS; i++){
           sber.setAccounts(new Account(String.valueOf(i), MONEY_IN_ACCOUNT));
        }

        vaska = alfa.getAccounts().get(String.valueOf(1));
        vitka = sber.getAccounts().get(String.valueOf(2));
        gena = sber.getAccounts().get(String.valueOf(3));
    }
    public void testSynch() {
        long expected = getMoneyInBank(alfa);
        for (int i = 0; i < CORES_AMOUNT; i++) {
            Thread t = new Thread(() -> {
                for (int n = 0; n < NUMBER_OF_OPERATIONS_FOR_THREAD; n++) {
                    alfa.transfer(String.valueOf(random.nextInt(alfa.getAccounts().size()) + 1), String.valueOf(random.nextInt(alfa.getAccounts().size()) + 1), FRAUD_LIMIT);
                }
            });
            t.start();
            try {
                t.join();
                assertEquals(expected, getMoneyInBank(alfa));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
        public void testSynchWithTwoClientsFromDifferentBanks(){
        Thread t1 = new Thread(()-> {
            alfa.transfer(vaska.getAccNumber(), vitka.getAccNumber(), 15000);});
        Thread t2 = new Thread(()-> {
            sber.transfer(vitka.getAccNumber(), vaska.getAccNumber(), 12000);});
        System.out.println("Деньги Василия " + vaska.getMoney());
        System.out.println("Деньги Витька " + vitka.getMoney());
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            System.out.println("Деньги Васьки после " + vaska.getMoney());
            System.out.println("Деньги Витька после " + vitka.getMoney());
            assertEquals(9997000, vaska.getMoney());
            assertEquals(10003000, vitka.getMoney());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testSynchWithTwoClientsFromSameBank(){
        Thread t3 = new Thread(()-> {
            sber.transfer(gena.getAccNumber(), vitka.getAccNumber(), 15000);});
        Thread t4 = new Thread(()-> {
            sber.transfer(vitka.getAccNumber(),  gena.getAccNumber(), 12000);});
        System.out.println("Деньги Гены " + gena.getMoney());
        System.out.println("Деньги Витька " + vitka.getMoney());
        t3.start();
        t4.start();
        try {
            t3.join();
            t4.join();
            System.out.println("Деньги Гены после " + gena.getMoney());
            System.out.println("Деньги Витька после " + vitka.getMoney());
            assertEquals(9997000, gena.getMoney());
            assertEquals(10003000, vitka.getMoney());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testDeadLock(){
        for (int i = 0; i < CORES_AMOUNT; i++) {
            Thread t = new Thread(() -> {
                for (int n = 0; n < NUMBER_OF_OPERATIONS_FOR_THREAD; n++) {
                    alfa.transfer(String.valueOf(random.nextInt(alfa.getAccounts().size()) + 1), String.valueOf(random.nextInt(alfa.getAccounts().size()) + 1), FRAUD_LIMIT);
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public long getMoneyInBank(Bank bank){
        long moneyInBank = 0;
        for (Account account : bank.getAccounts().values()){
            moneyInBank += account.getMoney();
        }
        return moneyInBank;
    }
}
