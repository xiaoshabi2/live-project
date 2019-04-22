package Filter;


import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        LotteryDrawRule lotteryDrawRule=new LotteryDrawRule("DEEP_FILTER","我要换组","2022-08-20 17:08:15","2022-10-29 13:17:48","2022-08-29 13:17:48", 10);
        LotteryDrawFilter lotteryDrawFilter=new LotteryDrawFilter(lotteryDrawRule,"H:\\javaweb代码\\LotteryDrawFilter\\src\\QQrecord-2022.txt");
        lotteryDrawFilter.doFilter();
    }
}
