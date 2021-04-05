package pisibg.utility;

import java.math.BigDecimal;
import java.math.MathContext;

public class Constants {
    public static final int START_PERSONAL_DISCOUNT_PERCENT = 5;
    public static final int MAX_PERSONAL_DISCOUNT_PERCENT = 10;
    public static final BigDecimal START_TURNOVER = new BigDecimal(0);
    public static final int DISCOUNT_INCREASE_TURNOVER_STEP = 1000;
    public static final int FIRST_ORDER_STATUS = 1;
    public static final int TWO_DECIMAL_PLACES = 2;
    public static final String DELETE = "DELETED";
    public static final MathContext mc = new MathContext(2);
}
