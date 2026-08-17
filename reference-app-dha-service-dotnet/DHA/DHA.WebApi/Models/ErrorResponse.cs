using System.Net;
using System.Runtime.Serialization;
using System.Text.Json.Serialization;

namespace DHA.WebApi.Models
{
    /// <summary>
    /// 
    /// </summary>
    public class ErrorResponse
    {
        /// <summary>
        /// Gets or Sets Code
        /// </summary>
        /// <example>500</example>
        [JsonPropertyName("code")]
        public int HttpStatusCode { get; set; }

        /// <summary>
        /// Gets or Sets Message
        /// </summary>
        /// <example>There was an exception</example>
        public string Message { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public ErrorResponse(HttpStatusCode code, string message)
        {
            HttpStatusCode = (int)code;
            Message = message;
        }
    }
}
