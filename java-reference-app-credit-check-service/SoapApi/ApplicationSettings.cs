using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SoapApi;

public class ApplicationSettings
{
    public string Username { get; set; } = null!;
    public string Password { get; set; } = null!;

    public FailureResponseSet FailureResponses { get; set; } = new();
    
    public class FailureResponseSet
    {
        public FailureResponse Error4xx { get; set; } = new();
        public FailureResponse Error5xx { get; set; } = new();
    }

    public class FailureResponse
    {
        public int FrequencyPercentage { get; set; } = 10;
    }
}
