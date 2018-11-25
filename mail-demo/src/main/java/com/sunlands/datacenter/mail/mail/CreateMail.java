package com.sunlands.datacenter.mail.mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateMail {
    public static void send(Properties config, Map<String, String> content) throws IOException, MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", config.getProperty("mail.host"));
        prop.setProperty("mail.transport.protocol", config.getProperty("mail.transport.protocol"));
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug("true".equals(config.getProperty("mail.Debug")) ? true : false);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、连上邮件服务器
        ts.connect(config.getProperty("mail.user"), config.getProperty("mail.password"));
        //4、创建邮件
        Message message = createMail(session, content);
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();

    }

    public static MimeMessage createMail(Session session, Map<String, String> content) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(content.get("fromAddress")));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(content.get("toAddress")));
        //邮件标题
        message.setSubject(content.get("subject"));

        //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content.get("content"), "text/html;charset=UTF-8");

        //创建容器描述数据关系
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.setSubType("mixed");
        if (content.get("filePath") != null) {
            MimeBodyPart attach = new MimeBodyPart();
            DataHandler dh = new DataHandler(new FileDataSource(content.get("filePath")));//
            attach.setDataHandler(dh);
            //attach.setFileName(content.get("attachName"));
            attach.setFileName(MimeUtility.encodeWord(content.get("attachName"), "UTF-8", null)); //附件中文名称
            mp.addBodyPart(attach);
        }
        message.setContent(mp);
        message.saveChanges();

        //返回生成的邮件
        return message;

    }

    public static void test() throws IOException, MessagingException {
        Properties config = new Properties();
        config.setProperty("mail.user","lixiang@sunlands.com");
        config.setProperty("mail.password","asdfghjkl248650");
        config.setProperty("mail.host","smtp.sunlands.com");
        config.setProperty("mail.Debug","true");
        config.setProperty("mail.transport.protocol","smtp");
        Map<String,String> content = new HashMap<String, String>();
        content.put("fromAddress","lixiang@sunlands.com");
        content.put("toAddress","wangshanshan@sunlands.com");
        content.put("subject","这是邮件主题");

        content.put("content","这是邮件正文");
        content.put("filePath","E:/测试excel.xlsx");
        content.put("attachName","这是邮件附件.xlsx");

        send(config,content);
    }
}
