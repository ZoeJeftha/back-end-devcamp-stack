using System.ComponentModel.DataAnnotations;

namespace kyc.Models;

/// <summary>
/// Instance of the error model
/// </summary>
public class Error
{
    /// <summary>
    /// The error status code
    /// </summary>
    [Required]
    public int Code { get; set; }

    /// <summary>
    /// The error message
    /// </summary>
    [Required]
    public string Message { get; set; } = string.Empty;
}
