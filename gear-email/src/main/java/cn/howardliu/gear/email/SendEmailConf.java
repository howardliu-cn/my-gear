package cn.howardliu.gear.email;

import org.apache.commons.lang3.Validate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <br>created at 16-3-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SendEmailConf {
    public static final String DEFAULT_PORT = "25";
    private final String host;
    private final String port;
    private final NameValuePair sendFrom;
    private final List<String> to = new ArrayList<>();
    private final List<String> cc = new ArrayList<>();
    private final List<String> bcc = new ArrayList<>();

    public SendEmailConf(String host, String sendFrom, String sendFromAs) {
        this(host, DEFAULT_PORT, sendFrom, sendFromAs);
    }

    public SendEmailConf(String host, String port, String sendFrom, String sendFromAs) {
        this.host = host;
        this.port = port;
        this.sendFrom = new NameValuePair(sendFrom, sendFromAs);
    }

    public SendEmailConf(String host, NameValuePair sendFrom, Collection<String> to) {
        this(host, DEFAULT_PORT, sendFrom, to);
    }

    public SendEmailConf(String host, String port, NameValuePair sendFrom, Collection<String> to) {
        this(host, port, sendFrom, to, new ArrayList<String>());
    }

    public SendEmailConf(String host, NameValuePair sendFrom, List<String> to, Collection<String> cc) {
        this(host, DEFAULT_PORT, sendFrom, to, cc);
    }

    public SendEmailConf(String host, String port, NameValuePair sendFrom, Collection<String> to,
            Collection<String> cc) {
        this(host, port, sendFrom, to, cc, new ArrayList<String>());
    }

    public SendEmailConf(String host, NameValuePair sendFrom, Collection<String> to, Collection<String> cc,
            Collection<String> bcc) {
        this(host, DEFAULT_PORT, sendFrom, to, cc, bcc);
    }

    public SendEmailConf(String host, String port, NameValuePair sendFrom, Collection<String> to, Collection<String> cc,
            Collection<String> bcc) {
        this.host = host;
        this.port = port;
        this.sendFrom = sendFrom;
        this.to.addAll(Validate.notEmpty(to, "收件人列表不能为空"));
        this.cc.addAll(cc == null ? new ArrayList<String>() : cc);
        this.bcc.addAll(bcc == null ? new ArrayList<String>() : bcc);
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public NameValuePair getSendFrom() {
        return sendFrom;
    }

    public List<String> getTo() {
        return to;
    }

    public InternetAddress[] getToAddress() throws AddressException {
        InternetAddress[] addresses = new InternetAddress[this.to.size()];
        for (int i = 0; i < this.to.size(); i++) {
            addresses[i] = new InternetAddress(this.to.get(i));
        }
        return addresses;
    }

    public List<String> getCc() {
        return cc;
    }

    public InternetAddress[] getCcAddress() throws AddressException {
        InternetAddress[] addresses = new InternetAddress[this.cc.size()];
        for (int i = 0; i < this.cc.size(); i++) {
            addresses[i] = new InternetAddress(this.cc.get(i));
        }
        return addresses;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public InternetAddress[] getBccAddress() throws AddressException {
        InternetAddress[] addresses = new InternetAddress[this.bcc.size()];
        for (int i = 0; i < this.bcc.size(); i++) {
            addresses[i] = new InternetAddress(this.bcc.get(i));
        }
        return addresses;
    }
}
