package pisibg.utility;

public class OffsetPageCalculator {
    public static int offsetPageCalculator(int perPage, int page){
        int offset = perPage*-1;
        for(int j = 0; j<page; j++){
            offset+=perPage;
        }
        return offset;
    }
}
