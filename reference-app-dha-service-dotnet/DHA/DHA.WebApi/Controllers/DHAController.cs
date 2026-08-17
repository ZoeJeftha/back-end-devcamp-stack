using DHA.Application.Features.Config;
using DHA.Application.Features.People;
using DHA.Application.Features.Rnd;
using DHA.WebApi.Filters;
using DHA.WebApi.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using System.Net;

namespace DHA.WebApi.Controllers
{
    /// <summary>
    /// Department of Home Affairs Checks
    /// </summary>
    [Authorize]
    [ApiController]
    [Route("status")]
    [Produces("application/json")]
    public class DHAController : ControllerBase
    {
        private readonly IPeopleService _peopleService;
        private readonly IRandomFailureService _randomFailureService;
        private readonly IOptions<ApplicationSettings> _config;

        /// <summary>
        /// Controller constructor
        /// </summary>
        /// <param name="peopleService"></param>
        /// <param name="randomFailureService"></param>
        /// <param name="config"></param>
        public DHAController(IPeopleService peopleService, IRandomFailureService randomFailureService, IOptions<ApplicationSettings> config)
        {
            _peopleService = peopleService;
            _randomFailureService = randomFailureService;
            _config = config;
        }

        /// <summary>
        /// Gets all People in the DHA "Database"
        /// </summary>
        /// <response code="200">Returns all current people</response>
        /// <response code="500">If there was a server side error</response>
        [ProducesResponseType(typeof(PersonResponse), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status500InternalServerError)]
        [HttpGet("people")]
        [DisableCustomErrorResponseFilter]
        public async Task<ActionResult> GetPeople()
        {
            var people = await _peopleService.GetAllPeople();
            var peopleResponse = people.Select(p => new PersonResponse(p));

            return Ok(peopleResponse);
        }

        /// <summary>
        /// Determine the Marital Status of a citizen given that citizens ID Number
        /// </summary>
        /// <param name="idNumber">The national id number of the citizen whose status we want to determine</param>
        /// <response code="200">Returns the current users marital status</response>
        /// <response code="500">If there was a server side error</response>
        /// <response code="404">If there was no matching person</response>
        [ProducesResponseType(typeof(MaritalStatusResponse), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status404NotFound)]
        [HttpGet("marital/{idNumber}")]
        [ServiceFilter(typeof(IDQueryParamValidationActionFilter))]
        public async Task<IActionResult> GetMaritalStatusById(long idNumber)
        {
            var person = await _peopleService.GetPersonWithId(idNumber);

            if (person == null)
            {
                return NotFound(new ErrorResponse(HttpStatusCode.NotFound, "The person with the specified ID was not found"));
            }

            var personMaritalStatusesViewModel = new MaritalStatusResponse(person.CurrentStatus, person.PreviousStatus);

            return Ok(personMaritalStatusesViewModel);
        }

        /// <summary>
        /// Determine if the citizen has a duplicate ID document issued
        /// </summary>
        /// <param name="idNumber">The national id number of the citizen whose status we want to determine</param>
        /// <response code="200">Returns a response indicating if the current user has a duplicated ID</response>
        /// <response code="500">If there was a server side error</response>
        /// <response code="404">If there was no matching person</response>
        [ProducesResponseType(typeof(DuplicateIDDocumentCheckResponse), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status404NotFound)]
        [HttpGet("duplicateId/{idNumber}")]
        [ServiceFilter(typeof(IDQueryParamValidationActionFilter))]
        public async Task<IActionResult> GetDuplicateIdDocumentStatusById(long idNumber)
        {
            var person = await _peopleService.GetPersonWithId(idNumber);

            if (person == null)
            {
                return NotFound(new ErrorResponse(HttpStatusCode.NotFound, "The person with the specified ID was not found"));
            }

            var personDuplicateIdViewModel = new DuplicateIDDocumentCheckResponse(person.HasDuplicatedId, person.DuplicateIDIssueDate);

            return Ok(personDuplicateIdViewModel);
        }

        /// <summary>
        /// Determine if the citizen is alive or deceased
        /// </summary>
        /// <param name="idNumber">The national id number of the citizen whose status we want to determine</param>
        /// <response code="200">Returns a response indicating if the current user is alive</response>
        /// <response code="500">If there was a server side error</response>
        /// <response code="404">If there was no matching person</response>
        [ProducesResponseType(typeof(LivingStatusResponse), StatusCodes.Status200OK)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(typeof(ErrorResponse), StatusCodes.Status404NotFound)]
        [HttpGet("living/{idNumber}")]
        [ServiceFilter(typeof(IDQueryParamValidationActionFilter))]
        public async Task<IActionResult> GetLivingStatusById(long idNumber)
        {
            var person = await _peopleService.GetPersonWithId(idNumber);

            if (person == null)
            {
                return NotFound(new ErrorResponse(HttpStatusCode.NotFound, "The person with the specified ID was not found"));
            }

            var personLivingStatusViewModel = new LivingStatusResponse(person.LivingStatus, person.DeceasedDate);

            return Ok(personLivingStatusViewModel);
        }
    }
}