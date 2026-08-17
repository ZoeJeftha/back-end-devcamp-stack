using DHA.WebApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using System.Net;

namespace DHA.WebApi.Filters
{
    public class CustomExceptionFilter : ExceptionFilterAttribute
    {
        private readonly ILogger<CustomExceptionFilter> _customExceptionFilterLogger;

        public CustomExceptionFilter(ILogger<CustomExceptionFilter> clustomExceptionFilter)
        {
            _customExceptionFilterLogger = clustomExceptionFilter;
        }

        public override void OnException(ExceptionContext context)
        {
            var errorResponse = new ErrorResponse(HttpStatusCode.InternalServerError, context.Exception.Message);

            _customExceptionFilterLogger.LogError("Unhandled exception occurred while executing request: {ex}", context.Exception);

            context.Result = new ObjectResult(errorResponse) { StatusCode = (int)HttpStatusCode.InternalServerError };
        }
    }
}
