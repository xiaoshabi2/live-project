package com.gwd.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @program: SuanFa
 * @description:
 * @author: ChenYu
 * @create: 2019-03-16 22:23
 **/
public class EmailUtil {
    // 发件人 账号和密码
    public static final String MY_EMAIL_ACCOUNT = "cy947205926@163.com";
    public static final String MY_EMAIL_PASSWORD = "cy947205926";// 密码,是你自己的设置的授权码

    public static final String subject = "程序员系统通知";
    public static final String remindAuditTemplate = "<!-- 审核提醒模板 -->\n" +
            "<div style=\"font-size: 60%;width: 700px;text-align:center;color: #666;border: 1px solid #e9e9e9;border-radius: 5px;\">\n" +
            "<h1 style=\"padding: 8px 15px;margin:0;border-radius: 5px;background: #3193DC;color: #fefefe;\">程序猿养成系统{方向}审核提醒</h1><br>\n" +
            "<p style=\"display:block;text-align:left;font-size:120%;margin-left:20px;\"><span style=\"color: #343131;font-weight:bold;\">{姓名}</span>提交了<span style=\"color: #3193DC;font-weight:bold;\">{任务}</span>：</p>\n" +
            "<div style=\"display: flex;flex-direction: row;\">\n" +
            "<p style=\"display:block;width:160px;text-align:left;margin-left:50px;\">博客文章地址：</p> <p style=\"color: #3193DC\">{博客地址}</p>\n" +
            "</div>\n" +
            "<div style=\"display: flex;flex-direction: row;\">\n" +
            "<p style=\"display:block;width:160px;text-align:left;margin-left:50px;\">GitHub仓库地址：</p><p style=\"color: #3193DC\">{GitHub仓库地址}</p>\n" +
            "</div><br>\n" +
            "<h2 style=\"padding: 5px 0;margin:0;color: #666;background: #f6f2f7;\">请您尽快审核</h2>\n" +
            "</div>";
    public static final String auditAccessTemplate = "<!-- 审核通过模板 -->\n" +
            "<div style=\"font-size: 60%;width: 780px;text-align:center;color: #666;border: 1px solid #e9e9e9;border-radius: 5px;\">\n" +
            "<h1 style=\"padding: 8px 15px;margin:0;border-radius: 5px;background: #3193DC;color: #fefefe;\">程序猿养成系统{方向}审核通过提醒</h1><br>\n" +
            "<p style=\"display:block;text-align:left;font-size:120%;margin-left:20px;\"><span style=\"color: #343131;font-weight:bold;\">{姓名}，您好，</span>您提交的<span style=\"color: #3193DC;font-weight:bold;\">{任务}</span>已经审核通过！新的任务已经解锁，<a href=\"http://www.finalexam.cn/tasksystem/login\" style=\"text-decoration: none;color:#3193DC;\">快去查看吧！</a></p><br>\n" +
            "</div>\n";

    // SMTP服务器(这里用的163 SMTP服务器)
    public static final String MEAIL_163_SMTP_HOST = "smtp.163.com";
    public static final String SMTP_163_PORT = "25";// 端口号,这个是163使用到的;QQ的应该是465或者875

    // 收件人
    public static final String RECEIVE_EMAIL_ACCOUNT = "947205926@qq.com";

//    public static void main(String[] args) throws AddressException, MessagingException {
//        Properties p = new Properties();
//        p.setProperty("mail.smtp.host", MEAIL_163_SMTP_HOST);
//        p.setProperty("mail.smtp.port", SMTP_163_PORT);
//        p.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
//        p.setProperty("mail.smtp.auth", "true");
//        p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");
//
//        Session session = Session.getInstance(p, new Authenticator() {
//            // 设置认证账户信息
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
//            }
//        });
//        session.setDebug(true);
//        System.out.println("创建邮件");
//        MimeMessage message = new MimeMessage(session);
//        // 发件人
//        message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));
//        // 收件人和抄送人
//        message.setRecipients(Message.RecipientType.TO, RECEIVE_EMAIL_ACCOUNT);
////		message.setRecipients(Message.RecipientType.CC, MY_EMAIL_ACCOUNT);
//
//        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉;多试几次吧)
//        message.setSubject("包裹");
//        message.setContent("<h1>李总,您好;你的包裹在前台</h1>", "text/html;charset=UTF-8");
//        message.setSentDate(new Date());
//        message.saveChanges();
//        System.out.println("准备发送");
//        Transport.send(message);
//    }
//

    public static void send(String subject,String content,String receiveEmail) throws AddressException, MessagingException {
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", MEAIL_163_SMTP_HOST);
        p.setProperty("mail.smtp.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");
        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));
        // 收件人和抄送人
        message.setRecipients(Message.RecipientType.TO, receiveEmail);
        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉;多试几次吧)
        message.setSubject(subject);
        message.setContent("<h1>"+ content +"</h1>", "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);
    }


    public static void send465(String subject,String content,String receiveEmail) throws AddressException, MessagingException {
        Properties p = new Properties();
        p.put("mail.smtp.ssl.enable", true);
        p.setProperty("mail.smtp.host", MEAIL_163_SMTP_HOST);
        p.setProperty("mail.smtp.port", "465");
        p.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");
        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));
        // 收件人和抄送人
        message.setRecipients(Message.RecipientType.TO, receiveEmail);
        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉;多试几次吧)
        message.setSubject(subject);
        message.setContent("<h1>"+ content +"</h1>", "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);
    }
}
