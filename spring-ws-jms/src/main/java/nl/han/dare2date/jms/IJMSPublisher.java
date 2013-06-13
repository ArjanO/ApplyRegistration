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
package nl.han.dare2date.jms;

import javax.jms.ObjectMessage;
import java.io.Serializable;

public interface IJMSPublisher {
    /**
     * Connect to the broker and open the topic.
     *
     * @param topicName Topic to open.
     * @return true if succeeded.
     */
    boolean connect(String topicName);

    /**
     * Disconnect from the broker and the topic.
     *
     * @return true if succeeded.
     */
    boolean disconnect();

    /**
     * Create a message object.
     *
     * @param item Serializable item.
     * @return Created ObjectMesssage instance.
     */
    ObjectMessage createObjectMessage(Serializable item);

    /**
     * Send a message to the topic or queue.
     *
     * @param message Message to publish in te topic or queue.
     * @return true if succeeded.
     */
    boolean send(ObjectMessage message);
}
