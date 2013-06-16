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

import nl.han.dare2date.applyregistrationservice.Registration;
import nl.han.dare2date.jms.IJMSSubscriber;
import nl.han.dare2date.logger.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Notify the other members of Dare2Date about the new member.
 */
public class AbonneeNotification implements MessageListener {
    private IJMSSubscriber subscriber;
    private ILogger logger;

    public static void main(String args[]) {
        ApplicationContext context = new ClassPathXmlApplicationContext("abonneeNotificationContext.xml");

        AbonneeNotification abonneeNotification = context.getBean("abonneeNotification", AbonneeNotification.class);

        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public AbonneeNotification(IJMSSubscriber subscriber) {
        this.subscriber = subscriber;

        if (subscriber.connect()) {
            subscriber.registerMessageListener("registered", this);
        }
    }

    /**
     * Set the logger.
     *
     * @param logger Logger.
     */
    @Autowired
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage msg = (ObjectMessage)message;

            try {
                if (msg.getObject() instanceof Registration) {
                    Registration r = (Registration)msg.getObject();

                    System.out.println(r.getUser().getEmail());
                }
            } catch (JMSException e) {
                logger.warn("Get object from message", e);
            }
        }
    }
}
