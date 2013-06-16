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
package nl.han.dare2date.service;

import junit.framework.Assert;
import nl.han.dare2date.applyregistrationservice.Registration;
import nl.han.dare2date.applyregistrationservice.User;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/userNotificationContext.xml")
public class UserNotificationTest {
    private ObjectMessage message;
    private User user;
    private Capture<SimpleMailMessage> mail;
    private MailSender sender;

    @Autowired
    private UserNotification userNotification;

    @Before
    public void before() throws JMSException {
        user = new User();
        user.setEmail("theus.de.tester@test.nl");
        user.setFirstname("Theus");
        user.setLastname("de Tester");

        Registration registration = new Registration();
        registration.setUser(user);

        mail = new Capture<SimpleMailMessage>();

        message = EasyMock.createMock(ObjectMessage.class);

        EasyMock.expect(message.getObject()).andReturn(registration).anyTimes();

        sender = EasyMock.createMock(MailSender.class);
        sender.send(EasyMock.capture(mail));

        userNotification.setMailSender(sender);
        userNotification.setRegistrationEmailFrom("r@dare2date.com");

        EasyMock.replay(sender);
        EasyMock.replay(message);
    }

    @After
    public void after() {
        EasyMock.verify(message);
        EasyMock.verify(sender);
    }

    @Test
    public void testOnMessage() {
        userNotification.onMessage(message);

        Assert.assertEquals("theus.de.tester@test.nl", mail.getValue().getTo()[0]);
        Assert.assertEquals("r@dare2date.com", mail.getValue().getFrom());
        Assert.assertTrue(mail.getValue().getText().indexOf("Theus") != -1);
    }
}
