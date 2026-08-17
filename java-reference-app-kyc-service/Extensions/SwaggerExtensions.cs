using Microsoft.OpenApi.Models;
using System.Reflection;

namespace Microsoft.AspNetCore.Builder;

internal static class SwaggerExtensions
{
    internal static IServiceCollection AddSwaggerWithXml(this IServiceCollection services)
    {
        services.AddSwaggerGen(config =>
        {
            config.SwaggerDoc("v1", new OpenApiInfo
            {
                Title = "KYC Status Checks",
                Version = "1.0.0"
            });
            
            var xmlDocumentFilePath = Path.Combine(AppContext.BaseDirectory, $"{Assembly.GetExecutingAssembly().GetName().Name}.xml");
            if (File.Exists(xmlDocumentFilePath))
            {
                config.IncludeXmlComments(xmlDocumentFilePath);
            }
        });

        return services;
    }

    internal static IApplicationBuilder UseSwaggerWithUI(this IApplicationBuilder app)
    {
        app.UseSwagger()
           .UseSwaggerUI(config => config.SwaggerEndpoint("/swagger/v1/swagger.json", "KYC Service API"));

        return app;
    }
}
