using System.Runtime.Serialization;

namespace DHA.WebApi.Models
{
    public class DuplicateIDDocumentCheckResponse
    {
        /// <summary>
        /// Gets or Sets HasDuplicateId
        /// </summary>
        /// <example>true</example>
        [DataMember(Name = "hasDuplicateId", EmitDefaultValue = true)]
        public bool HasDuplicateId { get; set; }

        /// <summary>
        /// Gets or Sets DuplicateIdIssueDate
        /// </summary>
        /// <example>2023-07-25</example>
        [DataMember(Name = "duplicateIdIssueDate", EmitDefaultValue = false)]
        public string? DuplicateIdIssueDate { get; set; }

        public DuplicateIDDocumentCheckResponse(bool hasDuplicateId, DateTime? duplicateIdIssueDate)
        {
            HasDuplicateId = hasDuplicateId;
            DuplicateIdIssueDate =  duplicateIdIssueDate?.ToString("yyyy-MM-dd");
        }
    }
}
