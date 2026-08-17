using DHA.Domain.Enums;

namespace DHA.Domain.Entities
{
    public class MaritalStatus
    {
        public MaritalStatuses Status { get; set; }
        public DateTime EffectiveFromDate { get; set; }
        public DateTime? EffectiveToDate { get; set; }

        public MaritalStatus(MaritalStatuses status, DateTime effectiveFromDate)
        {
            Status = status;
            EffectiveFromDate = effectiveFromDate;
            EffectiveToDate = null;
        }

        public MaritalStatus(MaritalStatuses status, DateTime effectiveFromDate, DateTime effectiveToDate)
        {
            Status = status;
            EffectiveFromDate = effectiveFromDate;
            EffectiveToDate = effectiveToDate;
        }
    }
}
