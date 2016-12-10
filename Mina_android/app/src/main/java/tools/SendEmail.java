package tools;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


/**
 * Created by _Tyhj on 2016/7/31.
 *
 * 有些新申请的账号，不能用来作为发件人
 *
 */
public class SendEmail {
    private static final String from = "tyhj@tyhj5.com";
    private static final String host = "smtp.tyhj5.com";
    private static final boolean isSSL = true;
    private static final int port = 25;
    private static final String username = "tyhj@tyhj5.com";
    private static final String password= "Han123456";

    private static SendEmail email = new SendEmail();

    public static SendEmail getInstance() {
        if (email == null) {
            email = new SendEmail();
            return email;
        }
        return email;
    }

    public static void sendEmail(String email2,String content){
        //发送邮件
            try {
                Email email = new SimpleEmail();
                //email.setSSLOnConnect(isSSL);
                email.setHostName(host);
                email.setSmtpPort(port);
                email.setAuthentication(username, password);
                email.setFrom(from);
                email.addTo(email2);
                email.setSubject("MSG邮箱验证");
                email.setMsg(content);
                email.send();
            } catch (EmailException e) {
                e.printStackTrace();
            }
            //System.out.println("发送完毕！");
    }
}
