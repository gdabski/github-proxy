# GitHub Proxy

Demo microservice accessing the public [V3 GitHub API](https://developer.github.com/v3/).

Per today's standards, making the application ready for deployment would foremost require enclosing it in a software container (e.g. Docker) and preparing relevant deployment infrastructure. This might also involve implementing some liveness/heartbeat/registering on the side of this service or exposing management functions. There are also aspects of further tuning network communication (own API versioning, headers, timeouts, caching GitHub responses based on Last-Modified or ETag). The JAR could be slimmed down by excluding some unused dependencies introduced by the Spring Boot starters. API documentation could be created (e.g. Swagger).

The functional/E2E/IT tests provided are just a fraction of what could be done even with one existing endpoint. It could for example also be verified how the application responds to corrupt responses from GitHub, too long response times, empty responses etc. Another possible area would be the service's response to requests with missing path elements (owner, name) or to invalid paths. The repetitiveness of endpoint paths and some RestAssured and WireMock API calls and the presence of injected server port value in the test class could be avoided by creating objects representing the tested and remote services, which would encapsulate this code and data and expose nicer APIs.

This project uses [Lombok](https://projectlombok.org/) and thus requires installing and/or configuring support in an IDE.

