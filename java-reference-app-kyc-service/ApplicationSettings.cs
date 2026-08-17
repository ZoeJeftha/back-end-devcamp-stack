namespace kyc_service
{
    /// <summary>
    /// Applications settings from configuration
    /// </summary>
    public class ApplicationSettings
    {
        /// <summary>
        /// Fixed set of failure responses
        /// </summary>
        public FailureResponseSet FailureResponses { get; set; } = new();

        /// <summary>
        /// Public key
        /// </summary>
        public string PublicKey { get; set; } = null!;
    
        /// <summary>
        /// A failure response set
        /// </summary>
        public class FailureResponseSet
        {
            /// <summary>
            /// 400-499 response errors
            /// </summary>
            public FailureResponse Error4xx { get; set; } = new();
            /// <summary>
            /// 500-599 response errors
            /// </summary>
            public FailureResponse Error5xx { get; set; } = new();
        }

        /// <summary>
        /// Percentage frequency to expect a failure response to manifest itself.
        /// </summary>
        public class FailureResponse
        {
            /// <summary>
            /// Percentage frequency.
            /// </summary>
            public int FrequencyPercentage { get; set; } = 10;
        }
    }
}
