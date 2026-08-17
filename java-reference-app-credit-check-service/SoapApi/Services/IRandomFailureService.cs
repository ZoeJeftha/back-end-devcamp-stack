using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SoapApi;

public interface IRandomFailureService
{
    bool IsFailure(int percent, int? deterministicSeed = null);
}
