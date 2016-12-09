package cn.howardliu.gear.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * <br>created at 16-3-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LoginAuthenticator extends Authenticator {
    private String userName = null;
    private String password = null;

    public LoginAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}