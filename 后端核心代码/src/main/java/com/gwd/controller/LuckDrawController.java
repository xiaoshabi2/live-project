package com.gwd.controller;

import com.aliyuncs.exceptions.ClientException;
import com.gwd.dao.DrawLuckResultDao;
import com.gwd.entity.*;
import com.gwd.util.EmailUtil;
import com.gwd.util.LcgRandom;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.sun.tools.doclets.internal.toolkit.builders.AnnotationTypeBuilder.ROOT;


@RequestMapping(value = "/drawluck")
@RestController
public class LuckDrawController {


    @Resource
    private DrawLuckResultDao drawLuckResultDao;

    /**
         name  抽奖名称
         document 文案
         winnerNum 抽奖人数
         startTime 开始时间
         endTime 结束时间
         resultTime  开奖时间
         keyWord    关键字
         filterType   过滤方式 1 不过滤  2 正常过滤 3 深度过滤
         award 奖品列表

     * */

    @RequestMapping("/draw")
    public ResponseData draw(String email,String name, String document, Integer winnerNum,
                             String startTime, String endTime, String resultTime,
                             String keyWord, Integer filterType, String award,
                             HttpServletRequest request) throws ClientException, IOException, MessagingException {
        ResponseData responseData = new ResponseData();
       LotteryDrawRule lotteryDrawRule=new LotteryDrawRule(LotteryDrawFilter.getFilterTypeString(filterType),keyWord,startTime,
               endTime,resultTime, winnerNum);
       LotteryDrawFilter lotteryDrawFilter=new LotteryDrawFilter(lotteryDrawRule,"/home/QQrecord-2022.txt");
        //LotteryDrawFilter lotteryDrawFilter=new LotteryDrawFilter(lotteryDrawRule,"G:\\MyJavaWeb\\Luckydraw\\src\\main\\resources\\QQrecord-2022.txt");
       Map<String, Integer> users = lotteryDrawFilter.doFilter();
       List<User> awardUsers = LcgRandom.getResult(users,winnerNum);
       String str[] = award.split("\\,");
       int j = 0;
       for(String s : str){
           String awardName = s.split(":")[0];
           Integer awardNum = Integer.valueOf(s.split(":")[1]);
           for (int i = 0; i<awardNum;i++){
               if(j<awardUsers.size()){
                   awardUsers.get(j).setAward(awardName);
                   j++;
               }
           }
       }
       StringBuilder awardString = new StringBuilder();
       for (User u: awardUsers) {
           awardString.append(u.toString()+"\r\n");
       }
       awardString.append(" ");
       DrawLuckResult dr = new DrawLuckResult(name,document,keyWord,startTime,endTime,resultTime,winnerNum,award,
               LotteryDrawFilter.getFilterTypeString(filterType),awardString.toString());
       drawLuckResultDao.insert(dr);
       responseData.setData(dr);
       Thread t = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   StringBuilder sb = new StringBuilder();
                   for (User u: awardUsers) {
                       sb.append(u.toString()+"</br>");
                   }
                   sb.append(" ");
                   EmailUtil.send465("中奖结果","<h1>中奖结果通知</h1></br>" + sb.toString(),email);
               } catch (MessagingException e) {
                   e.printStackTrace();
               }
               for (User u: awardUsers) {
                   try {
                       String name = u.getName();
                       String email = null;
                       if(name.contains("(")){
                           email = name.substring(name.indexOf("(") +1 ,name.lastIndexOf(")"));
                           if (!email.contains("@qq.com")){
                               email += "@qq.com";
                           }
                       }else if(name.contains("<")){
                           email = name.substring(name.indexOf("<") +1 ,name.lastIndexOf(">"));
                       }
                       // System.out.println(email);
                       // 为了不打扰其他人只通知自己
                       if (email.contains("947205926")){
                           EmailUtil.send465("中奖通知","恭喜" + u.getName() + "获得" + u.getAward(),"947205926@qq.com");
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }
       });
       t.start();
        return responseData;
    }



    @RequestMapping("/get/all")
    public ResponseData get(){
        ResponseData responseData = new ResponseData();
        responseData.setData(drawLuckResultDao.getAll());
        return responseData;
    }

    @RequestMapping("/get/{id}")
    public ResponseData getId(@PathVariable Integer id){
        ResponseData responseData = new ResponseData();
        responseData.setData(drawLuckResultDao.getById(id));
        return responseData;
    }

    @RequestMapping("/create/image/{id}")
    public ResponseData createId(@PathVariable Integer id) throws IOException, InterruptedException {
        ResponseData responseData = new ResponseData();
        DrawLuckResult dr = drawLuckResultDao.getById(id);
        executeLinuxCmd(dr.getAwardResult());
        return responseData;
    }

    public static void executeLinuxCmd(String argv) throws IOException, InterruptedException {
        Process process = null;
        String command1 = "python3 /root/getward/scholarship.py " + argv.replaceAll("\\r\\n","").replaceAll("\\n","");
        process = Runtime.getRuntime().exec(command1);
        process.waitFor();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/image")
    @ResponseBody
    public void getImage( HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File("/root/getward/img/scholarship.png");
        FileInputStream inputStream = new FileInputStream(file);
        int i = inputStream.available();
        //byte数组用于存放图片字节数据
        byte[] buff = new byte[i];
        inputStream.read(buff);
        //记得关闭输入流
        inputStream.close();
        //设置发送到客户端的响应内容类型
        response.setContentType("image/png");
        //response.setContentType("text/html; charset=utf-8");
        OutputStream out = response.getOutputStream();
        out.write(buff);
        //关闭响应输出流
        out.close();
    }
}