
# DHA

DHA is a API service that is used to mock out the Department of Home affairs services. You can use this service to:

 - Get All people available in the "Database"
 - Get a persons current Marital Status
 - Determine if a citizen has a duplicate ID Document which has been issued
 - Determine if a citizen is alive or deceased

## Installation

You will need the below tools before you can start working on this project:

 - .Net Core SDK (.NET 7 as of writing)
 - Visual Studio or VS Code
 - Docker (follow this [link](https://holocrons.entelect.co.za/code/local-development/docker-for-windows/) for installation instructions)


## Usage

Once you have your IDE and environment setup then you can run the project, to do this make sure that your startup project is DHA.WebApi and then click Run in Debug. You should then see a browser window open up at https://localhost:7080/swagger/index.html

## Contributing

Please start off by branching off of the master branch, then once your change is ready create a PR merging into the Master branch.


## Additional Notes

 - There is a excel file called IdNumbersDataReference.xlsx in the root of the repositority, this file contains a reference to all the static data the API contains.
 - All data in the api is static, there is no database or data file (such as .csv)
 - The ID Numbers are structured according to the guidelines provided [here](https://www.westerncape.gov.za/general-publication/decoding-your-south-african-id-number-0)
 - There is a postman collection that you can use for testing, to use it download postman and import the DHA.postman_collection.json collection into postman (remember to update the bearer token on the collection authorization section)

