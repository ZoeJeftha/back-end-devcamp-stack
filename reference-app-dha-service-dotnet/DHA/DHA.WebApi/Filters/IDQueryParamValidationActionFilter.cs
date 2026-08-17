using DHA.WebApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using System.Net;

namespace DHA.WebApi.Filters
{
    public class IDQueryParamValidationActionFilter : IActionFilter
    {
        public void OnActionExecuting(ActionExecutingContext context)
        {
            const string idNumber = "idNumber";
            string errorMessage = string.Empty;
            bool idParameterIsValid = true;

            var idParameter = context.ActionArguments.SingleOrDefault(aa => aa.Key == idNumber);

            if (idParameter.Value is null)
            {
                idParameterIsValid = false;
                errorMessage = $"The {idNumber} Parameter cannot be empty";
            }

            if (idParameterIsValid && idParameter.Value?.ToString()?.Length != 13)
            {
                idParameterIsValid = false;
                errorMessage = $"The length of the {idNumber} Parameter must be 13 digits long";
            }

            if (!idParameterIsValid)
            {
                var errorResponse = new ErrorResponse(HttpStatusCode.BadRequest, errorMessage);
                context.Result = new ObjectResult(errorResponse) { StatusCode = (int)HttpStatusCode.BadRequest };

                return;
            }
        }

        public void OnActionExecuted(ActionExecutedContext context) { }
    }
}
