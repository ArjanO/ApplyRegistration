package nl.han.dare2date.service.web;

import nl.han.dare2date.applyregistrationservice.ApplyRegistrationRequest;
import nl.han.dare2date.applyregistrationservice.ApplyRegistrationResponse;

import nl.han.dare2date.applyregistrationservice.Registration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

@Endpoint
public class ApplyRegistrationServiceEndpoint {
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

    private IConfirmRegistrationService confirmRegistrationService;
    private IValidateCreditcardService validateCreditcardService;

	public ApplyRegistrationServiceEndpoint(Marshaller marshaller,
			Unmarshaller unmarshaller) {
		this.marshaller = marshaller;
		this.unmarshaller = unmarshaller;
	}

    public void setConfirmRegistrationService(IConfirmRegistrationService confirmRegistrationService) {
        this.confirmRegistrationService = confirmRegistrationService;
    }

    public void setValidateCreditcardService(IValidateCreditcardService service) {
        this.validateCreditcardService = service;
    }

	@SuppressWarnings( { "unchecked", "deprecation" })
	@PayloadRoot(localPart = "ApplyRegistrationRequest", namespace = "http://www.han.nl/schemas/messages")
	public ApplyRegistrationResponse applyRegistration(ApplyRegistrationRequest req) {
        Registration reg = req.getRegistration();

        if (validateCreditcardService.validate(reg.getUser().getCard())) {
            confirmRegistrationService.confirm(reg);

            reg.setSuccesFul(true);
        } else {
            reg.setSuccesFul(false);
        }

        ApplyRegistrationResponse result = new ApplyRegistrationResponse();
        result.setRegistration(reg);

		return result;
	}
}
