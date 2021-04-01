package pisibg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pisibg.utility.SaleDiscountsAutomaticallyDeactivation;

@SpringBootApplication
public class PisibgApplication {

    public static void main(String[] args) {
        SpringApplication.run(PisibgApplication.class, args);
        SaleDiscountsAutomaticallyDeactivation saleDiscountsAutomaticallyDeactivation = new SaleDiscountsAutomaticallyDeactivation();
        saleDiscountsAutomaticallyDeactivation.setDaemon(true);
        saleDiscountsAutomaticallyDeactivation.start();
    }

}
