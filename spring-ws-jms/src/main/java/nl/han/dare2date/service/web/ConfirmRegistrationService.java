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

import nl.han.dare2data.logger.ILogger;
import nl.han.dare2date.applyregistrationservice.Registration;
import nl.han.dare2date.jms.IJMSPublisher;
import nl.han.dare2date.jms.JMSPublisher;
import nl.han.dare2date.service.jms.util.JMSUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

/**
 * Publish the registration as a JMS topic.
 */
public class ConfirmRegistrationService implements IConfirmRegistrationService {
    private ILogger logger;

    private IJMSPublisher publisher;

    /**
     * Set logger.
     *
     * @param logger Logger.
     */
    @Autowired
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    /**
     * Set JMS publisher.
     * @param publisher JMS publisher.
     */
    public void setPublisher(IJMSPublisher publisher) {
        this.publisher = publisher;
    }

    public void confirm(Registration reg) {
        if (reg == null) {
            throw new IllegalArgumentException();
        }

        publisher.connect("registered");

        ObjectMessage msg = publisher.createObjectMessage(reg);

        if (msg != null) {
            publisher.send(msg);
        }
    }
}
