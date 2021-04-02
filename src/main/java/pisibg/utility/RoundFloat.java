package pisibg.utility;

public class RoundFloat {
    public static double round(double amount, int decimalPlaces){
        int divider = 1;
        for(int i = 0; i<decimalPlaces; i++){
            divider*=10;
        }

        double sum = (double) Math.round(amount*divider)/divider;
        return sum;
    }
}
