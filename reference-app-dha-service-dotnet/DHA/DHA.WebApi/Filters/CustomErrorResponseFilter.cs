using DHA.Application.Features.Config;
using DHA.Application.Features.Rnd;
using DHA.WebApi.Models;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Options;
using System.Net;

namespace DHA.WebApi.Filters
{
    public class CustomErrorResponseFilter : IActionFilter
    {

        private readonly IOptions<ApplicationSettings> _config;
        private readonly IRandomFailureService _randomFailureService;

        public CustomErrorResponseFilter(IOptions<ApplicationSettings> config, IRandomFailureService randomFailureService)
        {
            _config = config;
            _randomFailureService = randomFailureService;
        }

        public void OnActionExecuted(ActionExecutedContext context) { }

        public void OnActionExecuting(ActionExecutingContext context) 
        {
            var shouldDisableErrorFilter = context.ActionDescriptor.FilterDescriptors.Any(x => x.Filter.GetType() == typeof(DisableCustomErrorResponseFilter));
            if (shouldDisableErrorFilter)
            {
                return;
            }
            
            if (_randomFailureService.IsFailure(_config.Value.FailureResponses.Error5xx.FrequencyPercentage))
            {
                var randomErrorCode = GetRandomError5xxCode();
                context.Result = new ObjectResult(new ErrorResponse(randomErrorCode, randomErrorCode.ToString()))
                {
                    StatusCode = (int)randomErrorCode
                };
            }
        }

        private static HttpStatusCode GetRandomError5xxCode()
        {
            Random random = new();
            var enumValues = Enum.GetValues(typeof(HttpStatusCode));
            (int min, int max) range = (55, 65);
            var randomError = (HttpStatusCode?)enumValues.GetValue(random.Next(range.min, range.max));
            if (randomError == null)
                return HttpStatusCode.NotFound;
            else
                return randomError.Value;
        }
    }
}
