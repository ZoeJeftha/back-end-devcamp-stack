
# Overview

- See the following classes for details on how Basic Authentication is implemented:  `BasicAuthenticationHandler`.
- `ICreditCheckService` is where the `ServiceContract` namespace is defined.
- `UseSoapEndpoint` method maps the interface methods to the `ServiceContract` and ultimately the `CreditCheckService`.

```csharp
app.UseSoapEndpoint<ICreditCheckService>(path: "...", 
    encoder: new SoapEncoderOptions()
);
```

# Postman SOAP request

To make a SOAP request from Postman, you need to configure the request appropriately to include the SOAP envelope and headers. Postman provides a feature called "raw" mode, which allows you to construct and send the SOAP request as an XML payload. Here's how you can configure the request in Postman:

1. Open Postman and create a new request or open an existing one.

2. Change the HTTP method to POST since SOAP requests are typically sent via HTTP POST.

3. Enter your SOAP endpoint URL in the request URL field (`https://example.com/ServicePath.asmx`).

4. Click on the "Body" tab.

5. In the "Body" tab, select the "raw" option from the available options.

6. Set the "Text" format to "XML (application/xml)".

7. Construct the SOAP envelope with the appropriate namespace and SOAP action based on your service contract (`entelect:devcamp:creditcheckservice`) and the method you want to call (e.g., `CreditCheck` with the `customerId` 64). Use the following template:

```xml
<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="entelect:devcamp:creditcheckservice">
    <soap:Body>
        <web:CreditCheck>
            <customerId>64</customerId>
        </web:CreditCheck>
    </soap:Body>
</soap:Envelope>
```

8. To configure basic authentication, you can set the authentication in Postman:

   - Click on the "Authorization" tab.
   - Choose "Basic Auth" from the "Type" dropdown.
   - Enter your username and password.

9. Click the "Send" button to make the SOAP request.

Postman will send the SOAP request to your specified endpoint with the constructed SOAP envelope in the request body. The SOAP service should respond with the corresponding SOAP response, which you will see in the Postman response panel.

### Example Response

```xml
<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <s:Body>
        <CreditCheckResponse xmlns="entelect:devcamp:creditcheckservice">
            <CreditCheckResult>GREEN</CreditCheckResult>
        </CreditCheckResponse>
    </s:Body>
</s:Envelope>
```