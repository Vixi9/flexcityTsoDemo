# Flexcity TSO Demo application

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Vixi9_flexcityTsoDemo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Vixi9_flexcityTsoDemo)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Vixi9_flexcityTsoDemo&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Vixi9_flexcityTsoDemo)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Vixi9_flexcityTsoDemo&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Vixi9_flexcityTsoDemo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Vixi9_flexcityTsoDemo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Vixi9_flexcityTsoDemo)

This project is part of a technical interview test

## Details

The REST API exposes a single HTTP endpoint from these specifications :

```
Create an HTTP endpoint for receiving the TSO request (also known as the activation) with the following information in the body:
- Date: date of the activation
- Volume: integer, corresponding of the number of kW needed
  Flexcity has a list of assets, defined by the following properties :
- Code: string
- Name: string
- Activation cost: double, price in case of activation, in €
- Availability: list of days in the week when the asset is available. For example an asset can only be available on Monday
- Volume: integer, correspond of the number of kW that can be activated for this asset
When receiving the activation from the TSO, the service must select the list of assets that will allow us to answer the demand.
For doing that, we need to select the assets with the cheapest prices, within the available ones for the date of the activation.
The service must answer to the endpoint with the list of assets to activate with their associated powers and prices.
If we don’t have enough assets to satisfy the demand, an error must be returned in the API.
```

The application is a monolith Spring Boot application with a REST API that exposes the endpoint `/api/activation` that
receives a GET request with a parameters containing the date and volume of the activation.
The application will then return a JSON response with a DTO containing a list of assets to activate with their
associated powers and prices.

The datasource of the application either uses a postgres database or an in-memory H2 database for tests purposes.
The postgresql database can be instantiated using a docker container which configuration can be found in
`src/main/docker/postgres.yaml`

As for the development methods I tried to apply Test driven development methodology.

As for the algorithm I tried to implement a recursive divide and conquer algorithm that will select the cheapest asset
for the problem.

At the moment, I didn't have time to implement memoization of sub problems which would greatly improve performance
issues, especially on bigger data samples. 