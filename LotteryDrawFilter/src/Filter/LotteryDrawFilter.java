package Filter;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LotteryDrawFilter {
    private Map<String, Integer> users = new HashMap<>();
    private String textType="USER_INFO";        //聊天记录行的类型   USER_INFO  ,USER_TALK_CONTENT
    private String userID=null;                 //用户ID，切割出来的姓名+账号；例雨勤未晴丶(275922130)
    private String filePath;                    //文件路径
    private LotteryDrawRule lotteryDrawRule;    //抽奖规则
    private InputStreamReader read = null;
    private String time;                        //聊天记录时间格式
    private String user;                        //聊天记录的用户id格式

    public Map doFilter() throws IOException {
        BufferedReader bufferedReader = openFile();
        //读取文件
        String talkContent = null;
        String temp = null;
        while ((temp = bufferedReader.readLine()) != null) {
            if (textType.equals("USER_TALK_CONTENT")) {
                if (!(isUserInfo(temp))) {
                    talkContent += temp;
                } else{
                    //判断发言是否有抽奖关键字
                    if (hasKeyWord(lotteryDrawRule.getKeyWord(), talkContent) && userID != null) {
                        talkContentFilter(talkContent);
                    }
                    talkContent = null;
                    userID = null;
                    textType = "USER_INFO";
                }
            }
            if (textType.equals("USER_INFO")) {
                userID = getUser(temp);
                if (userID != null) {
                    //去除系统消息、教师、助教
                    if (Pattern.matches("系统消息\\([0-9]+\\)", userID)
                            || Pattern.matches("教师_.*\\(.*\\)", userID)
                            || Pattern.matches("助教_.*\\(.*\\)", userID)) {
                        userID = null;
                    }
                }
                textType = "USER_TALK_CONTENT";
            }
        }
        for (String key : users.keySet()) {
            System.out.println(key + ":" + users.get(key));
        }
        read.close();
        return users;
    }

    public BufferedReader openFile(){
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(read);
            return bufferedReader;
        } else {
            System.out.println("找不到指定的文件");
        }
        return null;
    }

    //判断一行是不是用户信息行
    public boolean isUserInfo(String temp) {
        //匹配格式
        if (Pattern.matches(".*\\(.*\\)", temp) || Pattern.matches(".*\\<.*\\>", temp))
            return Pattern.matches(time + "\\s" + user, temp);//根据时间来匹配
        return false;
    }

    //判断是否包含#keyWord#
    public boolean hasKeyWord(String str, String temp) {
        if (temp == null) {
            return false;
        }
        return Pattern.matches(".*#" + str + "#.*", temp);
    }

    //判断这条聊天记录是不是在抽奖时间
    private boolean isTime(String nowTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date nowDate = format.parse(nowTime);
            Date startDate = format.parse(lotteryDrawRule.getStartTime());
            Date endDate = format.parse(lotteryDrawRule.getEndTime());
            if (nowDate.getTime() >= startDate.getTime() && nowDate.getTime()<= endDate.getTime()) {
                return true;
            }
            else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    //获取用户ID(姓名+账号)
    public String getUser(String temp) {
        Pattern p = Pattern.compile("(" + time + ")" + "\\s" + "(" + user + ")");
        Matcher matcher = p.matcher(temp);
        if (matcher.find()) {
            if (isTime(matcher.group(1))) {
                return matcher.group(2);
            }
        } else {
            System.out.println("匹配失败");
        }
        return null;
    }

    //打开文件
    public LotteryDrawFilter(LotteryDrawRule lotteryDrawRule,String filePath){
        this.lotteryDrawRule=lotteryDrawRule;
        this.filePath=filePath;
        time = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}\\s[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}";
        user = ".*[\\(\\<].*[\\)\\>]";
    }

       /*
        过滤函数
        filterType=NO_FILTER:表示不过滤；所有人参与抽奖
        filterType=NORMAL_FILTER:表示普通过滤；过滤只有抽奖关键字的用户
        filterType=DEEP_FILTER:表示深度过滤；过滤只有抽奖关键字的用户或降低只有图片+抽奖关键字的用户的获奖概率
		 */
    public void talkContentFilter(String talkContent) {
        boolean flag = true;    //判断其需不需要被过滤
        int deepNum = 0;        //满足深度过滤的次数
        //如果为NO_FILTER；不执行任何过滤
        if (!(lotteryDrawRule.getFilterType().equals("NO_FILTER"))) {
            //去除抽奖关键关键字
            talkContent = talkContent.substring(talkContent.lastIndexOf('#')+1);
            //符合NORMAL_FILTER
            if (talkContent == null || talkContent.equals("")) {
                flag = false;
            }
            //符合DEEP_FILTER
            if (talkContent.equals("[图片]") && lotteryDrawRule.getFilterType().equals("DEEP_FILTER")) {
                deepNum = 1;
            }
        }
        if (!users.containsKey(userID) && flag) { // 如果该用户id未出现过且不需要过滤
            users.put(userID, deepNum); // 存入map
        }
        else if (deepNum > 0) {       //如果该用户满足深度过滤的要求，就保存他的言论次数，用于计算概率时降低它的获奖权值
            deepNum = (int) users.get(userID) + 1;
            users.put(userID, deepNum);
        }
    }
}
