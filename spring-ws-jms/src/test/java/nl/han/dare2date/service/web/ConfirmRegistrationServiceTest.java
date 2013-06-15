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
package nl.han.dare2date.service.web;

import nl.han.dare2date.applyregistrationservice.Registration;
import nl.han.dare2date.jms.IJMSPublisher;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.ObjectMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-ws-servlet.xml")
public class ConfirmRegistrationServiceTest {
    private IJMSPublisher publisher;
    private ObjectMessage msg;

    @Autowired
    private ConfirmRegistrationService service;

    @Before
    public void before() {
        publisher = EasyMock.createMock(IJMSPublisher.class);
        msg = null;

        service.setPublisher(publisher);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfirmNull() {
        EasyMock.replay(publisher);

        service.confirm(null);
    }

    @Test
    public void testConfirm() {
        msg = EasyMock.createMock(ObjectMessage.class);

        EasyMock.expect(publisher.connect("registered"))
                .andReturn(true).once();

        EasyMock.expect(publisher.createObjectMessage(EasyMock.isA(Registration.class))).andReturn(msg).once();

        EasyMock.expect(publisher.send(msg)).andReturn(true).once();

        EasyMock.replay(publisher);
        EasyMock.replay(msg);

        service.confirm(new Registration());
    }

    @Test
    public void testNoObjectMessage() {
        EasyMock.expect(publisher.connect("registered"))
                .andReturn(true).once();

        EasyMock.expect(publisher.createObjectMessage(EasyMock.isA(Registration.class))).andReturn(null).once();

        EasyMock.replay(publisher);

        service.confirm(new Registration());
    }

    @After
    public void after() {
        if (publisher != null) {
            EasyMock.verify(publisher);
        }

        if (msg != null) {
            EasyMock.verify(msg);
        }
    }
}
