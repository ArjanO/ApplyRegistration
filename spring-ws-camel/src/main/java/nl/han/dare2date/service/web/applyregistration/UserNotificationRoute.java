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
package nl.han.dare2date.service.web.applyregistration;

import nl.han.dare2date.service.notifier.UserNotifier;
import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationResponse;
import nl.han.dare2date.service.web.applyregistration.model.Registration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class UserNotificationRoute extends RouteBuilder {
    private UserNotifier notifier;

    public void setNotifier(UserNotifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:topic:registered?jmsMessageType=Object&durableSubscriptionName=userNotification&clientId=12345").from("direct:usernotification")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        Registration r = exchange.getIn().getBody(ApplyRegistrationResponse.class).getRegistration();

                        notifier.notify(r);
                    }
                })
                ;
    }
}
