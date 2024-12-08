package foly.anhld.duan1.Email;


import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;


import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class SendEmail {

    private static final String API_KEY = " mXJB9M-rTDSMfC8gh3QwRg"; // Thay bằng API key

    // Phương thức gửi email chứa mã OTP
    public static void sendEmail(String toEmail, String otp) {
        Email from = new Email("your-email@domain.com"); // Địa chỉ email gửi
        String subject = "Mã OTP của bạn";
        Email to = new Email(toEmail); // Địa chỉ email người nhận
        Content content = new Content("text/plain", "Mã OTP của bạn là: " + otp); // Nội dung email
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            // Kiểm tra kết quả gửi email
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
