using Microsoft.AspNetCore.Authentication;

namespace SoapApi;

public class AuthenticationCheckMiddleware
{
    private readonly RequestDelegate _next;

    public AuthenticationCheckMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task Invoke(HttpContext context)
    {
        
        string requesturl = context.Request.QueryString.ToString();
        Console.WriteLine("invoking " + requesturl);
        if (requesturl.Contains("wsdl", StringComparison.OrdinalIgnoreCase)) {
            Console.WriteLine("service wsdl document");
        } else {
            var authenticateResult = await context.AuthenticateAsync();

            if (!authenticateResult.Succeeded)
            {
                context.Response.StatusCode = 401;
                await context.Response.WriteAsync("Unauthenticated");
                await context.Response.Body.FlushAsync();
                return;
            }
        }
        await _next(context);
    }
}
