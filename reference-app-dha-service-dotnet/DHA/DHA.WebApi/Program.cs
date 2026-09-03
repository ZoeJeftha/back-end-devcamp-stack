using DHA.Application.Features.Config;
using DHA.Application.Features.People;
using DHA.Application.Features.Rnd;
using DHA.WebApi.Filters;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Reflection;
using System.Security.Cryptography;
using System.Text.Json.Serialization;

var builder = WebApplication.CreateBuilder(args);
builder.Configuration.AddEnvironmentVariables();
var config = builder.Configuration;

var keyToRSAParameters = (string key) =>
{
    var keyBase64 = key
            .Replace("-----BEGIN PUBLIC KEY-----", "")
            .Replace("-----END PUBLIC KEY-----", "")
                .Replace("\r", "")
                .Replace("\n", "")
                .Trim();
    Console.WriteLine($"KEY LENGTH: {key.Length}");
    Console.WriteLine($"KEY START: {key.Substring(0, Math.Min(50, key.Length))}");
    Console.WriteLine($"KEY END: {key.Substring(Math.Max(0, key.Length - 50))}");

    byte[] keyBytes = Convert.FromBase64String(keyBase64);

    Console.WriteLine($"DECODED KEY LENGTH: {keyBytes.Length}");
    Console.WriteLine($"FIRST BYTE: {keyBytes[0]:X2}");

    using (var rsa = new RSACryptoServiceProvider())
    {
        rsa.ImportSubjectPublicKeyInfo(keyBytes, out _);
        return rsa.ExportParameters(false);
    }
};

builder.Logging.AddConsole();
builder.Logging.AddDebug();

var logger = LoggerFactory.Create(config =>
{
    config.AddConsole();
    config.AddDebug();
}).CreateLogger("Program");

try
{
    // Convert the SSH RSA public key to RSAParameters
    RSAParameters publicKeyParameters = keyToRSAParameters(config["Application:PublicKey"]!);

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
    builder.Services.AddControllers(options =>
    {
        options.Filters.Add<CustomExceptionFilter>();
        options.Filters.Add<CustomErrorResponseFilter>();
    }).AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
    });
    // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
    builder.Services.AddEndpointsApiExplorer();
    builder.Services.AddSwaggerGen(options =>
    {
        options.SwaggerDoc("v1", new Microsoft.OpenApi.Models.OpenApiInfo
        {
            Version = "1.0.0",
            Title = "Department of Home Affairs Checks",
        });

        var xmlFilename = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
        options.IncludeXmlComments(Path.Combine(AppContext.BaseDirectory, xmlFilename));
        options.SchemaFilter<EnumSchemaFilter>();
    });

    builder.Services.AddScoped<IRandomFailureService, RandomFailureService>();
    builder.Services.AddSingleton<IPeopleService, PeopleService>();
    builder.Services.Configure<ApplicationSettings>(builder.Configuration.GetSection("Application"));
    builder.Services.AddScoped<IDQueryParamValidationActionFilter>();


    var app = builder.Build();
//
//    if (app.Environment.IsDevelopment())
//    {
        app.UseSwagger();
        app.UseSwaggerUI();
    //}

    app.UseHttpsRedirection();
    app.UseAuthentication();
    app.UseAuthorization();

    app.MapControllers();

    app.Run();
}
catch (Exception exception)
{
    logger.LogError(exception, "An exception has occurred during program startup, see inner exception for details");
}

