using DHA.Domain.Entities;

namespace DHA.Application.Features.People
{
    public interface IPeopleService
    {
        public Task<Person> GetPersonWithId(long id);
        public Task<List<Person>> GetAllPeople();
    }
}
