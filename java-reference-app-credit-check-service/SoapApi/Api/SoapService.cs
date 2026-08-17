using System.ServiceModel;

namespace SoapApi;

public abstract class SoapService
{
    // In the context of SOAP web services, the standard SOAP fault that most closely associates with the 
    // HTTP "NotFound" response (HTTP 404 Not Found) is the "Client" fault with the fault code "Client."
    public FaultException NotFound(string reason) => ClientError(reason, "Not Found");
    public FaultException BadRequest(string reason) => ClientError(reason);
    public FaultException InternalServerError(string reason) => ServerError(reason);

    private FaultException ClientError(string reason, string httpResponseStatus = "Bad Request")
    {
        // Standard SOAP FaultCode "Client"
        // This fault is used to indicate errors on the client-side.
        // It can include various types of client-related errors, such as invalid request parameters or
        // missing required data.
        var fault = new FaultReason($"{httpResponseStatus}: {reason}.");
        var code = new FaultCode("Client");

        throw new FaultException(fault, code, String.Empty);
    }

    private FaultException ServerError(string reason, string httpResponseStatus = "Internal Server Error")
    {
        // Standard SOAP FaultCode "Server"
        // This fault is used to indicate errors on the server-side.
        // It can include server-related errors, such as database connection failures or internal server errors.
        // Create a SOAP fault and throw a FaultException
        var fault = new FaultReason($"{httpResponseStatus}: {reason}.");
        var code = new FaultCode("Server");

        throw new FaultException(fault, code, String.Empty);
    }
}
