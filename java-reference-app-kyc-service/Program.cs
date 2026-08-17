using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;
using System.Text.Json.Serialization;
using kyc_service;
using kyc_service.Services;

var builder = WebApplication.CreateBuilder(args);
builder.Configuration.AddEnvironmentVariables();
var config = builder.Configuration;

var keyToRSAParameters = (string key) =>
{
    var keyBase64 = key
            .Replace("-----BEGIN PUBLIC KEY-----", "")
            .Replace("-----END PUBLIC KEY-----", "");
    byte[] keyBytes = Convert.FromBase64String(keyBase64);

    using (var rsa = new RSACryptoServiceProvider())
    {
        rsa.ImportSubjectPublicKeyInfo(keyBytes, out _);
        return rsa.ExportParameters(false);
    }
};

// Convert the SSH RSA public key to RSAParameters
RSAParameters publicKeyParameters = keyToRSAParameters(config["Application:PublicKey"]!);

// Enable logging
builder.Logging.ClearProviders();
builder.Logging.AddConsole(); // Add Console logging provider

// Add JWT authentication
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        IssuerSigningKey = new RsaSecurityKey(publicKeyParameters),
        ValidateIssuerSigningKey = true,
        ValidateIssuer = false,
        ValidateAudience = false,
        ValidateLifetime = true,
    };
});

builder.Services.AddAuthorization();
builder.Services.AddControllers()
                .AddJsonOptions(options =>
                {
                    options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
                });
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerWithXml();
builder.Services.Configure<ApplicationSettings>(builder.Configuration.GetSection("Application"));
builder.Services.AddScoped<IRandomFailureService, RandomFailureService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwaggerWithUI();
}

app.UseDeveloperExceptionPage();
app.UseHttpsRedirection();
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
