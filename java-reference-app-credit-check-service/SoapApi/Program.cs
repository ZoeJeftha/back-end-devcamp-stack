using Microsoft.AspNetCore.Authentication;
using SoapApi;
using SoapCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSoapCore();
builder.Services.Configure<ApplicationSettings>(builder.Configuration.GetSection("Application"));
builder.Services.AddScoped<ICreditCheckService, CreditCheckService>();
builder.Services.AddScoped<IRandomFailureService, RandomFailureService>();
builder.Services.AddHttpContextAccessor();
builder.Services.AddMvc();

// Add basic authentication
builder.Services.AddAuthentication("BasicAuthentication")
        .AddScheme<AuthenticationSchemeOptions, BasicAuthenticationHandler>("BasicAuthentication", null);
    
var app = builder.Build();

app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();
app.UseMiddleware<AuthenticationCheckMiddleware>();

app.UseSoapEndpoint<ICreditCheckService>(path: "/CreditCheck", 
    encoder: new SoapEncoderOptions()
);

app.Run();
