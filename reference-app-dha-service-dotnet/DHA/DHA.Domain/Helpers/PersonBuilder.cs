using DHA.Domain.Entities;
using DHA.Domain.Enums;

namespace DHA.Domain.Helpers
{
    public class PersonBuilder
    {
        private Person _person { get; set; }
        private Random randomGenerator = new(0);

        public PersonBuilder(long idNumber)
        {
            if (idNumber.ToString().Length != 13)
            {
                throw new Exception("The Length of an ID Number must be 13 digits long");
            }

            _person = new Person(idNumber, GenerateRandomDate());
        }

        private bool IsPersonValidToBeBuild()
        {
            return _person != null && _person.CurrentStatus != null;
        }

        public PersonBuilder SetLivingStatusToAlive()
        {
            _person.LivingStatus = LivingStatuses.Alive;
            _person.DeceasedDate = null;

            return this;
        }

        public PersonBuilder SetLivingStatusToDeceased()
        {
            _person.LivingStatus = LivingStatuses.Deceased;
            _person.DeceasedDate = DateTime.Now.Date;

            return this;
        }

        public PersonBuilder SetDuplicateIdStatusToDuplicated()
        {
            _person.HasDuplicatedId = true;
            _person.DuplicateIDIssueDate = DateTime.Now.Date;

            return this;
        }


        public PersonBuilder SetPreviousMaritalStatusToMarried()
        {
            var randomNumberOfDays = randomGenerator.Next(720);
            var randomPreviousDate = _person.CurrentStatus.EffectiveFromDate.Subtract(TimeSpan.FromDays(randomNumberOfDays));
            _person.PreviousStatus = new MaritalStatus(MaritalStatuses.Married, randomPreviousDate, _person.CurrentStatus.EffectiveFromDate);

            return this;
        }

        public PersonBuilder SetPreviousMaritalStatusToDivorced()
        {
            _person.PreviousStatus = new MaritalStatus(MaritalStatuses.Divorced, DateTime.Now);

            return this;
        }

        public PersonBuilder SetPreviousMaritalStatusToWidowed()
        {
            _person.PreviousStatus = new MaritalStatus(MaritalStatuses.Widowed, DateTime.Now);

            return this;
        }

        public PersonBuilder SetCurrentMaritalStatusToMarried()
        {
            _person.PreviousStatus = _person.CurrentStatus;
            var effectiveFromDate = GenerateRandomDate(_person.CurrentStatus.EffectiveFromDate);
            _person.PreviousStatus.EffectiveToDate = effectiveFromDate;
            _person.CurrentStatus = new MaritalStatus(MaritalStatuses.Married, effectiveFromDate);

            return this;
        }

        public PersonBuilder SetCurrentMaritalStatusToDivorced()
        {
            _person.PreviousStatus = _person.CurrentStatus;
            var effectiveFromDate = GenerateRandomDate(_person.CurrentStatus.EffectiveFromDate);
            _person.PreviousStatus.EffectiveToDate = effectiveFromDate;
            _person.CurrentStatus = new MaritalStatus(MaritalStatuses.Divorced, effectiveFromDate);

            return this;
        }

        public PersonBuilder SetCurrentMaritalStatusToWidowed()
        {
            _person.PreviousStatus = _person.CurrentStatus;
            var effectiveFromDate = GenerateRandomDate(_person.CurrentStatus.EffectiveFromDate);
            _person.PreviousStatus.EffectiveToDate = effectiveFromDate;
            _person.CurrentStatus = new MaritalStatus(MaritalStatuses.Widowed, effectiveFromDate);

            return this;
        }

        public Person Build(long idNumber, bool clear = true)
        {
            if (idNumber.ToString().Length != 13)
            {
                throw new Exception("The Length of an ID Number must be 13 digits long");
            }

            if (IsPersonValidToBeBuild()) 
            {
                var personToReturn = _person;
                if (clear)
                {
                    _person = new Person(idNumber, GenerateRandomDate());
                }

                return personToReturn;
            }

            return new Person(idNumber, new MaritalStatus(MaritalStatuses.Married, GenerateRandomDate()));
        }

        private DateTime GenerateRandomDate(DateTime? dateTime = null)
        {
            DateTime start = dateTime ?? new (1980, 1, 1);
            var range = (DateTime.Today - start).Days;
            DateTime generatedDate = start.AddDays(randomGenerator.Next(range));
            return generatedDate;
        }
    }
}