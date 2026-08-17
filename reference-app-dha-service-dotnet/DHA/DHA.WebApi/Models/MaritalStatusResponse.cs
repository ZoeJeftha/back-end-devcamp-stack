using DHA.Domain.Entities;

namespace DHA.WebApi.Models
{
    /// <summary>
    /// 
    /// </summary>
    public class MaritalStatusResponse
    {
        /// <summary>
        /// Gets or Sets CurrentStatus
        /// </summary>[Required]
        public MaritalStatusDto CurrentStatus { get; set; }

        /// <summary>
        /// Gets or Sets PreviousStatus
        /// </summary>
        public MaritalStatusDto PreviouStatus { get; set; }

        public MaritalStatusResponse(MaritalStatus currentStatus, MaritalStatus? previouStatus)
        {
            CurrentStatus = new MaritalStatusDto(currentStatus);

            if (previouStatus != null)
            {
                PreviouStatus = new MaritalStatusDto(previouStatus);
            }
        }
    }
}
