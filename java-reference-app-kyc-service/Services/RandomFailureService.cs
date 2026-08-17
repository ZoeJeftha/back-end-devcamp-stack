namespace kyc_service.Services
{
    /// <summary>
    /// Random Failure/Success outcomes
    /// </summary>
    public class RandomFailureService : IRandomFailureService
    {
        /// <summary>
        /// IsFailure method
        /// </summary>
        /// <param name="percent"></param>
        /// <param name="deterministicSeed"></param>
        /// <returns>Boolean calculation value determining whether an operation will fail or succeed.</returns>
        public bool IsFailure(int percent, int? deterministicSeed = null)
        {
            Random random;
            if (deterministicSeed is not null)
            {
                // deterministic, and should return the same outcome for the same seed.
                random = new Random(deterministicSeed.Value);
            }
            else
            {
                // roll the dice.
                random = new Random();
            }

            // Working on 100% (and always discarding the decimal) a fail should be simulated
            // whenever the randomly generated number is below the percentage integer passed in.
            var rnd = random.Next(100);

            return rnd <= percent;
        }
    }
}


