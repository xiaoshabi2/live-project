package Filter;

public class LotteryDrawRule {
    private String filterType;      //过滤器类型：NO_FILTER ,NORMAL_FILTER  ,DEEP_FILTER
    private String keyWord;	        //设置抽奖的关键词
    private String startTime;	    //设置抽奖的开始时间
    private String endTime;         //设置抽奖的结束时间
    private String resultTime;      //设置抽奖结果公布时间
    private int winnerNum;          //设置获奖人数

    public LotteryDrawRule(String filterType, String keyWord, String startTime, String endTime, String resultTime, int winnerNum) {
        this.filterType = filterType;
        this.keyWord = keyWord;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
        this.winnerNum = winnerNum;
    }

    public LotteryDrawRule() {
    }

    public String getFilterType() {
        return filterType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getResultTime() {
        return resultTime;
    }

    public int getWinnerNum() {
        return winnerNum;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public void setWinnerNum(int winnerNum) {
        this.winnerNum = winnerNum;
    }
}
