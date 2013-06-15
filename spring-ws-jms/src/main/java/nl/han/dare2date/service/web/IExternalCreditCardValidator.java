package nl.han.dare2date.service.web;

import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Joell
 * Date: 13-6-13
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public interface IExternalCreditCardValidator {
    ObjectMessage getReplyMessage();
    void handleMessage(Serializable contents);
}
