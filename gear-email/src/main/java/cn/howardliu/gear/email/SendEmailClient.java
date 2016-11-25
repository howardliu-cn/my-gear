package cn.howardliu.gear.email;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

/**
 * <br/>created at 16-3-18
 *
 * @author liuxh
 * @since 1.1.4
 */
public class SendEmailClient {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailClient.class);
    private SendEmailConf conf;
    private String username;
    private String password;
    private final boolean validate;

    public SendEmailClient(SendEmailConf conf) {
        this(conf, null, null);
    }

    public SendEmailClient(SendEmailConf conf, String username, String password) {
        this.conf = conf;
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            this.validate = false;
        } else {
            this.username = Validate.notBlank(username, "验证用户名不能为空");
            this.password = Validate.notBlank(password, "验证密码不能为空");
            this.validate = true;
        }
    }

    public void sendEmail(String subject, String content, Collection<String> to) throws MessagingException {
        this.sendEmail(subject, content, to, new ArrayList<String>(), new ArrayList<String>());
    }

    public void sendEmail(String subject, String content, Collection<String> to, Collection<String> cc,
            Collection<String> bcc)
            throws MessagingException {
        this.conf.getTo().addAll(to);
        this.conf.getCc().addAll(cc);
        this.conf.getBcc().addAll(bcc);
        this.sendEmail(subject, content);
    }

    public void sendEmail(String subject, String content) throws MessagingException {
        logger.debug("准备发送邮件，发件人{}，收件人{}，抄送{}，秘密抄送：{}，主题{}，内容：{}",
                this.conf.getSendFrom(), this.conf.getTo(), this.conf.getCc(), this.conf.getBcc(), subject, content);
        Properties props = new Properties();
        props.put("mail.smtp.host", this.conf.getHost());
        props.put("mail.smtp.port", this.conf.getPort());
        props.put("mail.smtp.auth", this.validate);

        Session session;
        if (this.validate) {
            LoginAuthenticator loginAuthenticator = new LoginAuthenticator(this.username, this.password);
            session = Session.getDefaultInstance(props, loginAuthenticator);
        } else {
            session = Session.getDefaultInstance(props);
        }
        //session.setDebug(true);// 打印Debug信息

        MimeMessage message = new MimeMessage(session);
        try {
            NameValuePair sendFrom = this.conf.getSendFrom();
            message.setFrom(new InternetAddress(sendFrom.getName(), sendFrom.getValue(), "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        message.setRecipients(Message.RecipientType.TO, this.conf.getToAddress());
        message.setRecipients(Message.RecipientType.CC, this.conf.getCcAddress());
        message.setRecipients(Message.RecipientType.BCC, this.conf.getBccAddress());

        message.setSubject(subject, "UTF-8");

        Multipart mp = new MimeMultipart();
        MimeBodyPart mbpContent = new MimeBodyPart();
        mbpContent.setText(content, "UTF-8");
        mp.addBodyPart(mbpContent);
        message.setContent(mp, "text/html;charset=utf-8");

        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);
        logger.info("邮件发送成功，主题：{}，内容：{}", subject, content);
    }
}
