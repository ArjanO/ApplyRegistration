package nl.han.dare2date.service.web;

import nl.han.dare2date.applyregistrationservice.Creditcard;
import nl.han.dare2date.service.jms.util.JMSUtil;
import nl.han.dare2date.service.jms.util.Queues;
import nl.han.dare2date.service.jms.util.Requestor;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import java.io.Serializable;

/**
 * 
 * @author mdkr
 *
 * Is used as a JMS client using request-reply
 */
public class ValidateCreditcardService extends Requestor {

    private Creditcard cc;

    public ValidateCreditcardService() throws JMSException, NamingException{
        super(JMSUtil.getConnection(), Queues.REQUEST_QUEUE, Queues.REPLY_QUEUE, Queues.INVALID_QUEUEU);
    }

	public boolean validate(Creditcard cc) {
        boolean isValid = false;
        this.cc = cc;
        try {
            this.send();
            this.receiveSync();
            isValid = getReplyMessage().getBooleanProperty("valid");
            getSession().close();
        } catch(JMSException e) {
            e.printStackTrace();
        }
        System.out.println("VALIDATION");
        System.out.println(isValid);

        return isValid;
	}

    @Override
    public ObjectMessage getObjectMessage() {
        ObjectMessage msg = null;
        try{
            msg = getSession().createObjectMessage(cc);}
        catch(JMSException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public Serializable getResponse() {
        return null;
    }
}