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
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.jms.ObjectMessage;

public class UserNotifierTest {
    private UserNotifier notifier;
    private Registration registration;
    private User user;
    private Capture<SimpleMailMessage> mail;
    private MailSender sender;

    @Before
    public void before() {
        user = new User();
        user.setEmail("theus.de.tester@test.nl");
        user.setFirstname("Theus");
        user.setLastname("de Tester");

        registration = new Registration();
        registration.setUser(user);

        mail = new Capture<SimpleMailMessage>();

        sender = EasyMock.createMock(MailSender.class);
        sender.send(EasyMock.capture(mail));

        notifier = new UserNotifier();
        notifier.setMailSender(sender);
        notifier.setFromEmail("r@dare2date.com");

        EasyMock.replay(sender);
    }

    @After
    public void after() {
        EasyMock.verify(sender);
    }

    @Test
    public void testNotify() throws Exception {
        notifier.notify(registration);

        Assert.assertEquals("theus.de.tester@test.nl", mail.getValue().getTo()[0]);
        Assert.assertEquals("r@dare2date.com", mail.getValue().getFrom());
        Assert.assertTrue(mail.getValue().getText().indexOf("Theus") != -1);
    }
}
