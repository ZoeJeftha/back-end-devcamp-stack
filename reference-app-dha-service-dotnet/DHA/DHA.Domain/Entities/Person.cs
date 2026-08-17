using DHA.Domain.Enums;

namespace DHA.Domain.Entities
{
    public class Person
    {
        public long IDNumber { get; set; }
        public MaritalStatus CurrentStatus { get; set; }
        public MaritalStatus? PreviousStatus { get; set; }
        public LivingStatuses LivingStatus { get; set; }
        public DateTime? DeceasedDate { get; set; }
        public bool HasDuplicatedId { get; set; }
        public DateTime? DuplicateIDIssueDate { get; set; }

        public Person(long iDNumber, DateTime effectiveFromDate)
        {
            IDNumber = iDNumber;
            LivingStatus = LivingStatuses.Alive;
            HasDuplicatedId = false;
            DeceasedDate = null;
            DuplicateIDIssueDate = null;
            CurrentStatus = new MaritalStatus(MaritalStatuses.Single, effectiveFromDate);
            PreviousStatus = null;
        }

        public Person(long iDNumber, MaritalStatus currentStatus)
        {
            IDNumber = iDNumber;
            CurrentStatus = currentStatus;
            PreviousStatus = null;
            LivingStatus = LivingStatuses.Alive;
            DeceasedDate = null;
            PreviousStatus = new MaritalStatus(MaritalStatuses.Single, DateTime.Now.AddDays(-1));
        }
    }
}
