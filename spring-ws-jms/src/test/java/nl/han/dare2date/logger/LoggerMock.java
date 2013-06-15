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
package nl.han.dare2date.logger;

import org.easymock.EasyMock;
import org.springframework.beans.factory.FactoryBean;

public class LoggerMock implements FactoryBean<ILogger> {
    public ILogger getObject() throws Exception {
        ILogger mock = EasyMock.createMock(ILogger.class);

        mock.debug(EasyMock.isA(String.class));
        mock.debug(EasyMock.isA(String.class), EasyMock.isA(Throwable.class));

        mock.warn(EasyMock.isA(String.class));
        mock.warn(EasyMock.isA(String.class), EasyMock.isA(Throwable.class));

        mock.error(EasyMock.isA(String.class));
        mock.error(EasyMock.isA(String.class), EasyMock.isA(Throwable.class));

        mock.info(EasyMock.isA(String.class));
        mock.info(EasyMock.isA(String.class), EasyMock.isA(Throwable.class));

        EasyMock.replay(mock);
        return mock;
    }

    public Class<?> getObjectType() {
        return ILogger.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
