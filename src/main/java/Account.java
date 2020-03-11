import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account
{
    private String accNumber;
    private long money;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        if (money > 0)
        this.money = money;
        else throw new IllegalArgumentException();
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        Pattern pat = Pattern.compile("[0-9]*");
        Matcher mat = pat.matcher(accNumber);
        if (mat.matches())
        this.accNumber = accNumber;
        else throw new IllegalArgumentException();
    }


}
