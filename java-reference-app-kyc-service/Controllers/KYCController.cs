using kyc.Models;
using kyc_service;
using kyc_service.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.AspNetCore.Authorization;
using System.ComponentModel.DataAnnotations;

namespace kyc.Controllers
{
    /// <summary>
    /// The KYC Status check controller
    /// </summary>
    [Authorize]
    [ApiController]
    [Route("kyc")]
    [Produces("application/json")]
    public class KYCController : ControllerBase
    {
        private readonly ApplicationSettings _settings;
        private readonly IRandomFailureService _randomFailureService;

        /// <summary>
        /// Constructor methods
        /// </summary>
        public KYCController(IRandomFailureService randomFailureService, IOptions<ApplicationSettings> config)
        {
            _settings = config.Value;
            _randomFailureService = randomFailureService;
        }

        /// <summary>
        /// Find out the KYC Status of a customer based off of their Id
        /// </summary>
        /// <param name="customerId">The id of the customer whose KYC status we want to retrieve</param>
        /// <response code="200">Expected response to a valid request</response>
        /// <response code="0">Unexpected error</response>
        /// <returns>An instance of <see cref="KYC"/></returns>
        [HttpGet]
        [Route("{customerId}")]
        [ProducesResponseType(typeof(KYC), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(Error), 0)]
        public IActionResult GetKYCStatusById([FromRoute(Name = "customerId")][Required] string customerId)
        {
            // 20% of all requests result in a 5xx range error
            if (_randomFailureService.IsFailure(_settings.FailureResponses.Error5xx.FrequencyPercentage))
            {
                // This only ever returns 500 (Internal Server Error).
                // Perhaps randomise this to return various different 5xx range errors
                return StatusCode(StatusCodes.Status500InternalServerError, new Error()
                {
                    Code = StatusCodes.Status500InternalServerError,
                    Message = "An error occurred on the server."
                });
            }

            // 10% of all customerIds result in a 404 (Not Found) error
            // Importantly though, this randomisation is deterministic using the customerId
            // as random seed to ensure that the same response is returned for the same
            // customerId, every time.
            var success = int.TryParse(customerId, out var id);

            if (success)
            {
                if (_randomFailureService.IsFailure(_settings.FailureResponses.Error4xx.FrequencyPercentage, id))
                {
                    return NotFound(new Error()
                    {
                        Code = StatusCodes.Status404NotFound,
                        Message = "Customer was not found"
                    });
                }

                var result = new KYC(id);
                return Ok(result);
            }
            else
            {
                return BadRequest(new Error()
                {
                    Code = StatusCodes.Status400BadRequest,
                    Message = "Unable to parse customer id"
                });
            }
        }
    }
}
