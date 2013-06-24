/**
 * Copyright (c) 2013 HAN University of Applied Sciences
 * Arjan Oortgiese
 * Joëll Portier
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package nl.han.dare2date.service.notifier;

import nl.han.dare2date.service.web.applyregistration.model.Registration;
import nl.han.dare2date.service.web.applyregistration.model.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Notifies the user about the succeeded registration.
 */
public class UserNotifier {
    private MailSender mailSender;
    private String fromEmail;

    /**
     * Set the from e-mail address.
     *
     * @param address Sender e-mail address.
     */
    public void setFromEmail(String address) {
        this.fromEmail = address;
    }

    /**
     * Set the mail sender.
     *
     * @param mailSender Mail sender.
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notify(Registration registration) {
        if (registration == null) {
            throw new IllegalArgumentException();
        }

        User user = registration.getUser();

        StringBuffer sbText = new StringBuffer();
        sbText.append(String.format("Hoi %s", user.getFirstname()));
        sbText.append("\n\n");
        sbText.append("Welkom bij Dare2Date.");
        sbText.append("\n\n");
        sbText.append("Hierbij bevestigen wij uw registratie bij Dare2Date.");
        sbText.append("\n\n");
        sbText.append("Met vriendelijke groet,");
        sbText.append("\n\n");
        sbText.append("Het Dare2Date Team");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(fromEmail);
        mail.setSubject("Je registratie bij Dare2Date");
        mail.setText(sbText.toString());

        mailSender.send(mail);
    }
}
