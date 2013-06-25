package nl.han.dare2date.service.web.applyregistration;


import nl.han.dare2date.service.web.applyregistration.model.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreditcardValidatorRouteTest extends CamelTestSupport {
    private ApplyRegistrationRequest registrationRequest;
    private Registration registration;
    private User user;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new CreditCardValidatorRoute();
    }

    @Before
    public void before() {
        registrationRequest = new ApplyRegistrationRequest();

        registration = new Registration();

        user = new User();

        registration.setUser(user);
        registrationRequest.setRegistration(registration);
    }

    @Test
    public void testValidCreditcard() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip("mock:result");
            }
        });

        Creditcard cc = new Creditcard();
        cc.setNumber(79927398713L);
        cc.setCvc(123);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar date = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        date.setYear(getCurrentYear() + 1);

        cc.setValidThrough(date);

        user.setCard(cc);

        getMockEndpoint("mock:result").expectedMessageCount(1);

        ApplyRegistrationResponse response = (ApplyRegistrationResponse)template.requestBody("direct:creditcard",
                registrationRequest);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getRegistration());
        Assert.assertTrue(response.getRegistration().isSuccesFul());

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testInvalidCreditcard() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip("mock:result");
            }
        });

        Creditcard cc = new Creditcard();
        cc.setNumber(79927398712L);
        cc.setCvc(123);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar date = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        date.setYear(getCurrentYear() + 4);

        cc.setValidThrough(date);

        user.setCard(cc);

        getMockEndpoint("mock:result").expectedMessageCount(1);

        ApplyRegistrationResponse response = (ApplyRegistrationResponse)template.requestBody("direct:creditcard",
                registrationRequest);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getRegistration());
        Assert.assertFalse(response.getRegistration().isSuccesFul());

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testInvalidDateCreditcard() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpointsAndSkip("mock:result");
            }
        });

        Creditcard cc = new Creditcard();
        cc.setNumber(79927398713L);
        cc.setCvc(123);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar date = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        date.setYear(getCurrentYear() - 1);

        cc.setValidThrough(date);

        user.setCard(cc);

        getMockEndpoint("mock:result").expectedMessageCount(1);

        ApplyRegistrationResponse response = (ApplyRegistrationResponse)template.requestBody("direct:creditcard",
                registrationRequest);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getRegistration());
        Assert.assertFalse(response.getRegistration().isSuccesFul());

        assertMockEndpointsSatisfied();
    }

    private int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
