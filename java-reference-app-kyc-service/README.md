# KYC Service

KYC is a API service that is used to mock out the Know Your Customer service. You can use this service to:

- Get the KYC Status of a customer based off of their Id

## Installation

You will need the below tools before you can start working on this project:

- .Net Core SDK (.NET 7 as of writing)
- Visual Studio or VS Code
- Docker (follow this [link](https://holocrons.entelect.co.za/code/local-development/docker-for-windows/) for installation instructions)


## Usage

### Configure application settings

Ensure that you update the `Application.PublicKey` configuration variable in the [appsettings.json](https://bitbucket.org/entelect-software/kyc-service/src/master/appsettings.json) file with the appropriate public key (See [Authentication Public Key](https://bitbucket.org/entelect-software/authentication-service/src/master/src/main/resources/app.pub)).

### Running the application

Once you have your IDE and environment setup then you can run the project by simply launching the Docker launch profile in Debug mode. You should then see a browser window open up with the **Swagger** documentation

## Contributing

Please start off by branching off of the master branch, then once your change is ready create a PR merging into the Master branch.

