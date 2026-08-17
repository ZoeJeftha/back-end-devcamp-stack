using DHA.Domain.Entities;
using DHA.Domain.Enums;
using System.Runtime.Serialization;

namespace DHA.WebApi.Models
{
    /// <summary>
    /// 
    /// </summary>
    public class PersonResponse
    {
        /// <summary>
        /// Gets or Sets IDNumber
        /// </summary>
        /// <example>9001010000081</example>
        [DataMember(Name = "IDNumber", EmitDefaultValue = false)]
        public long IDNumber { get; set; }

        /// <summary>
        /// Gets or Sets Status
        /// </summary>
        public MaritalStatusDto CurrentStatus { get; set; }

        /// <summary>
        /// Gets or Sets Status
        /// </summary>
        [DataMember(Name = "PreviousStatus", EmitDefaultValue = false)]
        public MaritalStatusDto? PreviousStatus { get; set; }

        /// <summary>
        /// Gets or Sets LivingStatus
        /// </summary>
        public LivingStatuses LivingStatus { get; set; }

        /// <summary>
        /// Gets or Sets DeceasedDate
        /// </summary>
        /// <example></example>
        public DateTime? DeceasedDate { get; set; }

        /// <summary>
        /// Gets or Sets HasDuplicatedId
        /// </summary>
        /// <example>false</example>
        public bool HasDuplicatedId { get; set; }

        /// <summary>
        /// Gets or Sets DuplicateIDIssueDate
        /// </summary>
        /// <example>2023-04-04</example>
        public DateTime? DuplicateIDIssueDate { get; set; }

        public PersonResponse(Person person)
        {
            IDNumber = person.IDNumber;
            CurrentStatus = new MaritalStatusDto(person.CurrentStatus);
            PreviousStatus = person.PreviousStatus != null ? new MaritalStatusDto(person.PreviousStatus) : null;
            LivingStatus = person.LivingStatus;
            DeceasedDate = person.DeceasedDate;
            HasDuplicatedId = person.HasDuplicatedId;
            DuplicateIDIssueDate = person.DuplicateIDIssueDate;
        }
    }
}
