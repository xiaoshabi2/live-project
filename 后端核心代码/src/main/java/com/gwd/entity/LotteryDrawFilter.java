package com.gwd.entity;

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
    private String textType="USER_INFO";            //聊天记录行的类型   USER_INFO  ,USER_TALK_CONTENT
    private String userID=null;
    private String filePath;            //文件路径
    private LotteryDrawRule lotteryDrawRule;  //抽奖规则
    private InputStreamReader read = null;
    private String time;
    private String user;

    private final static String NO_FILTER = "NO_FILTER"; // 表示不过滤；所有人参与抽奖
    private final static String NORMAL_FILTER = "NO_FILTER"; // 表示普通过滤；筛除只参与抽奖而无发表任何原创言论的用户，鼓励大家积极参与有意义的发言
    private final static String DEEP_FILTER = "DEEP_FILTER" ;  // 表示深度过滤；只参与抽奖而无发表任何原创言论，只参与抽奖且只发送图片。

    public static String getFilterTypeString(int type){
        if(type == 1){
            return NO_FILTER;
        }else if(type == 2){
            return NORMAL_FILTER;
        }else{
            return DEEP_FILTER;
        }
    }


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
                    if (hasKey(lotteryDrawRule.getKeyWord(), talkContent) && userID != null) { // 发言"#...#"的抽奖关键字
                        handleMap(talkContent);
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
                    if (Pattern.matches("系统消息\\([0-9]+\\)", userID) || Pattern.matches("教师_.*\\(.*\\)", userID) || Pattern.matches("助教_.*\\(.*\\)", userID)) {
                        userID = null;
                    }
                }
                textType = "USER_TALK_CONTENT";
            }
        }
        read.close();
        return users;
    }

    //判断一行是不是用户信息行
    public boolean isUserInfo(String temp) {
        //匹配格式
        if (Pattern.matches(".*\\(.*\\)", temp) || Pattern.matches(".*\\<.*\\>", temp))
            return Pattern.matches(time + "\\s" + user, temp);//根据时间来匹配
        return false;
    }

    //获取用户
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

    public boolean hasKey(String str, String temp) {
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

    public LotteryDrawFilter(LotteryDrawRule lotteryDrawRule,String filePath){
        this.lotteryDrawRule=lotteryDrawRule;
        this.filePath=filePath;
        time = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}\\s[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}";
        user = ".*[\\(\\<].*[\\)\\>]";
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

    /*
        filterType=NO_FILTER:表示不过滤；所有人参与抽奖
        filterType=NORMAL_FILTER:表示普通过滤；筛除只参与抽奖而无发表任何原创言论的用户，鼓励大家积极参与有意义的发言。
        filterType=DEEP_FILTER:表示深度过滤；只参与抽奖而无发表任何原创言论，只参与抽奖且只发送图片。
     */
    public void handleMap(String talkContent) {
        boolean flag = true;
        int num = 0;
        if (!(lotteryDrawRule.getFilterType().equals("NO_FILTER"))) {
            talkContent = talkContent.substring(talkContent.lastIndexOf('#')+1);
            if (talkContent == null || talkContent.equals("")) { // 如果除去关键字后发送内容为空-->符合普通过滤规则
                flag = false;
            }
            if (talkContent.equals("[图片]") && lotteryDrawRule.getFilterType().equals("DEEP_FILTER")) {
                num = 1;
            }
        }
        if (!users.containsKey(userID) && flag) { // 如果该id未出现过
            users.put(userID, num); // 存入map
        } else if (num > 0) {
            num = (int) users.get(userID) + 1;
            users.put(userID, num);
        }
    }


}
