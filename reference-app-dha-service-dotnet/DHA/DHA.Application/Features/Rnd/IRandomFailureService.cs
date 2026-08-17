using System;

namespace DHA.Application.Features.Rnd
{
    /// <summary>
    /// Random Failure/Success outcomes service interface
    /// </summary>
    public interface IRandomFailureService
    {
        /// <summary>
        /// IsFailure method
        /// </summary>
        /// <param name="percent"></param>
        /// <param name="deterministicSeed"></param>
        /// <returns>Boolean calculation value determining whether an operation will fail or succeed.</returns>
        bool IsFailure(int percent, int? deterministicSeed = null);
    }
}
