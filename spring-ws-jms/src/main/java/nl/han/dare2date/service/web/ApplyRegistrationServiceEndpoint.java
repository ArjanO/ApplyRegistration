package nl.han.dare2date.service.web;

import nl.han.dare2date.applyregistrationservice.ApplyRegistrationRequest;
import nl.han.dare2date.applyregistrationservice.ApplyRegistrationResponse;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

@Endpoint
public class ApplyRegistrationServiceEndpoint {
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

    private IConfirmRegistrationService confirmRegistrationService;

	public ApplyRegistrationServiceEndpoint(Marshaller marshaller,
			Unmarshaller unmarshaller) {
		this.marshaller = marshaller;
		this.unmarshaller = unmarshaller;
	}

    public void setConfirmRegistrationService(IConfirmRegistrationService confirmRegistrationService) {
        this.confirmRegistrationService = confirmRegistrationService;
    }

	@SuppressWarnings( { "unchecked", "deprecation" })
	@PayloadRoot(localPart = "ApplyRegistrationRequest", namespace = "http://www.han.nl/schemas/messages")
	public ApplyRegistrationResponse applyRegistration(ApplyRegistrationRequest req) {
        confirmRegistrationService.confirm(req.getRegistration());

		return new ApplyRegistrationResponse();
	}
}
