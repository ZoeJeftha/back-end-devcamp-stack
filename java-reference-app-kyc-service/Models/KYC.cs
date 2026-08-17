using System.ComponentModel.DataAnnotations;
using kyc.Enums;

namespace kyc.Models;

/// <summary>
/// Instance of the KYC model
/// </summary>
public class KYC
{
    /// <summary>
    /// The primary indicator
    /// </summary>
    [Required]
    public bool PrimaryIndicator { get; set; }

    /// <summary>
    /// The secondary indicator
    /// </summary>
    [Required]
    public bool SecondaryIndicator { get; set; }

    /// <summary>
    /// The tax compliance status
    /// </summary>
    [Required]
    public TaxComplianceStatus TaxCompliance { get; set; }

    /// <summary>
    /// Constructor that instantiates the KYC class with deterministic randomised
    /// member variable values based on a supplied random seed
    /// </summary>
    /// <param name="seed">Seed value for randomising the class members</param>
    public KYC(int seed)
    {
        Random random = new Random(seed);

        // Generate random values for member variables
        // TODO: Confirm business rules in terms of valid combinations of values
        PrimaryIndicator = random.Next(2) == 0;
        SecondaryIndicator = random.Next(2) == 0;
        TaxCompliance = (TaxComplianceStatus)random.Next(3);
    }
}
