using DHA.Domain.Entities;
using DHA.Domain.Helpers;

namespace DHA.Application.Features.People
{
    public class PeopleService : IPeopleService
    {
        public List<Person> _people { get; set; } = new List<Person>();

        public PeopleService()
        {
            AddPeople();
        }

        public async Task<Person> GetPersonWithId(long id)
        {
            var person = _people.SingleOrDefault(p => p.IDNumber == id);

            return await Task.FromResult(person);
        }

        public async Task<List<Person>> GetAllPeople()
        {
            return await Task.FromResult(_people);
        }

        private void AddPeople()
        {
            var personBuilder = new PersonBuilder(9001010000081);

            var aliveNoDuplicatedIDSingle = personBuilder.Build(9102021000182);
            var aliveNoDuplicatedIDMarried = personBuilder.SetCurrentMaritalStatusToMarried().Build(9203032000180);
            var aliveNoDuplicatedIdCSDivorced = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().Build(2908207100180); 
            var aliveNoDuplicatedIdCSWidowed = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().Build(2807197000081);
            var aliveWithDuplicatedIDCSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetDuplicateIdStatusToDuplicated().Build(2706186111182);
            var aliveDuplicatedIdCSDivorced = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetDuplicateIdStatusToDuplicated().Build(2605176110083);
            var aliveDuplicatedIdCSWidowed = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetDuplicateIdStatusToDuplicated().Build(9607072200087);
            var aliveNoDuplicatedIdCSDivorcedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetPreviousMaritalStatusToMarried().Build(9708082220188);
            var aliveNoDuplicatedIdCSWidowedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetPreviousMaritalStatusToMarried().Build(9809092222089);
            var aliveWithDuplicatedIdCSDivorcedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetPreviousMaritalStatusToMarried().SetDuplicateIdStatusToDuplicated().Build(9910103333180);
            var aliveWithDuplicatedIdCSWidowedPSMarried = personBuilder.SetPreviousMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetPreviousMaritalStatusToMarried().SetDuplicateIdStatusToDuplicated().Build(8909217110081);

            var deadNoDuplicatedIDMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetLivingStatusToDeceased().Build(2112125100188);
            var deadNoDuplicatedIdCSDivorced = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetLivingStatusToDeceased().Build(2201135110087);
            var deadNoDuplicatedIdCSWidowed = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetLivingStatusToDeceased().Build(2302145111186);
            var deadWithDuplicatedIDCSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetDuplicateIdStatusToDuplicated().SetLivingStatusToDeceased().Build(2403156000085);
            var deadDuplicatedIdCSDivorced = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetDuplicateIdStatusToDuplicated().SetLivingStatusToDeceased().Build(2504166100184);
            var deadDuplicatedIdCSWidowed = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetDuplicateIdStatusToDuplicated().SetLivingStatusToDeceased().Build(9506062000186); 
            var deadNoDuplicatedIdCSDivorcedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetPreviousMaritalStatusToMarried().SetLivingStatusToDeceased().Build(9405051111085); 
            var deadNoDuplicatedIdCSWidowedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetPreviousMaritalStatusToMarried().SetLivingStatusToDeceased().Build(9304041100184); 
            var deadWithDuplicatedIdCSDivorcedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToDivorced().SetPreviousMaritalStatusToMarried().SetDuplicateIdStatusToDuplicated().SetLivingStatusToDeceased().Build(9203031000093); 
            var deadWithDuplicatedIdCSWidowedPSMarried = personBuilder.SetCurrentMaritalStatusToMarried().SetCurrentMaritalStatusToWidowed().SetPreviousMaritalStatusToMarried().SetDuplicateIdStatusToDuplicated().SetLivingStatusToDeceased().Build(2011115000089); 

            _people = new List<Person>()
            {
                aliveNoDuplicatedIDSingle,
                aliveNoDuplicatedIDMarried,
                aliveNoDuplicatedIdCSDivorced,
                aliveNoDuplicatedIdCSWidowed,
                aliveWithDuplicatedIDCSMarried,
                aliveDuplicatedIdCSDivorced,
                aliveDuplicatedIdCSWidowed,
                aliveNoDuplicatedIdCSDivorcedPSMarried,
                aliveNoDuplicatedIdCSWidowedPSMarried,
                aliveWithDuplicatedIdCSDivorcedPSMarried,
                aliveWithDuplicatedIdCSWidowedPSMarried,
                deadNoDuplicatedIDMarried,
                deadNoDuplicatedIdCSDivorced,
                deadNoDuplicatedIdCSWidowed,
                deadWithDuplicatedIDCSMarried,
                deadDuplicatedIdCSDivorced,
                deadDuplicatedIdCSWidowed,
                deadNoDuplicatedIdCSDivorcedPSMarried,
                deadNoDuplicatedIdCSWidowedPSMarried,
                deadWithDuplicatedIdCSDivorcedPSMarried,
                deadWithDuplicatedIdCSWidowedPSMarried
            };
        }
    }
}
