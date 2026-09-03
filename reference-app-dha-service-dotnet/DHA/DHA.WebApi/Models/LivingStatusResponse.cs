using DHA.Domain.Enums;
using System.Runtime.Serialization;

namespace DHA.WebApi.Models
{
    /// <summary>
    /// 
    /// </summary>
    public class LivingStatusResponse
    {
        /// <summary>
        /// Gets or Sets VarLivingStatus
        /// </summary>
        public LivingStatuses LivingStatus { get; set; }

        /// <summary>
        /// Gets or Sets DeceasedDate
        /// </summary>
        /// <example></example>
        public string? DeceasedDate { get; set; }

        public LivingStatusResponse(LivingStatuses livingStatus, DateTime? deceasedDate)
        {
            LivingStatus = livingStatus;
            DeceasedDate = deceasedDate?.ToString("yyyy-MM-dd");
        }
    }
}
