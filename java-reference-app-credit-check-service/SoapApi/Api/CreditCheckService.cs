using Microsoft.Extensions.Options;

namespace SoapApi;

public class CreditCheckService : SoapService, ICreditCheckService
{
    private readonly ApplicationSettings _applicationSettings;
    private readonly IRandomFailureService _randomFailureService;
    private readonly HttpContext _httpContext;

    private readonly IHttpContextAccessor httpContextAccessor;

    public CreditCheckService(IRandomFailureService randomFailureService, IOptions<ApplicationSettings> config, IHttpContextAccessor httpContextAccessor)
    {
        this.httpContextAccessor = httpContextAccessor;
        _applicationSettings = config.Value;
        _randomFailureService = randomFailureService;
        _httpContext = httpContextAccessor.HttpContext ?? throw new ArgumentNullException("HttpContext cannot be null");
    }

    public string CreditCheck(int customerId)
    {
        var failed =_randomFailureService.IsFailure(_applicationSettings.FailureResponses.Error4xx.FrequencyPercentage, customerId);
        if (failed) NotFound("The resource could not be found");

        var rand = new Random(customerId).Next(99);

        if (rand < 33) {
            return  "GREEN";
        }

        if (rand >= 33 && rand <= 66) {
            return  "AMBER";
        }

        return "RED";
    }
}
