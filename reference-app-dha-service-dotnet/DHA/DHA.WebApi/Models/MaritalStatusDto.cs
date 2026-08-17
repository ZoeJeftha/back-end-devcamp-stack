using DHA.Domain.Entities;
using DHA.Domain.Enums;
using System.Text.Json.Serialization;

namespace DHA.WebApi.Models
{
    /// <summary>
    /// 
    /// </summary>
    public class MaritalStatusDto
    {
        /// <summary>
        /// Gets or Sets Status
        /// </summary>
        /// <example>Married</example>
        [JsonPropertyName("status")]
        public MaritalStatuses MaritalStatus { get; set; }

        /// <summary>
        /// Gets or Sets EffectiveFromDate
        /// </summary>
        /// <example>2023-07-24</example>
        [JsonPropertyName("effectiveFrom")]
        public string EffectiveFromDate { get; set; }

        /// <summary>
        /// Gets or Sets EffectiveToDate
        /// </summary>
        /// <example>2023-07-25</example>
        [JsonPropertyName("effectiveTo")]
        public string? EffectiveToDate { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public MaritalStatusDto(MaritalStatus maritalStatus)
        {
            MaritalStatus = maritalStatus.Status;
            EffectiveFromDate = maritalStatus.EffectiveFromDate.ToString("yyyy-MM-dd");
            EffectiveToDate = maritalStatus.EffectiveToDate?.ToString("yyyy-MM-dd") ?? string.Empty;
        }
    }

}