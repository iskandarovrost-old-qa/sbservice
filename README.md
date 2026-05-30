# Project Description
This project presents SIMPLE SpringBoot REST API services with integration API tests.

The purpose is to show difference between different integration test approaches:
* simple Junit
* libraries like Rest Assured

## Services
#### Article parser 
runs on `POST /api/ParseArticle` with the body of JSON
``
{"articleBody":"some text"}
``

It searches person and his actions in the text.

Tests: jUnit with mockMvc

#### Simple async Service
provides 2 endpoints `POST /api/SimpleAsync/setAsyncResult` and `GET /api/SimpleAsync/getAsyncResult`

Tests: jUnit with `SpringBootTest` and `RestTestClient` bean

#### Async Serwice with Polling
provides 2 endpoins `POST /api/PollingAsync/startTask` and `GET /api/PollingAsync/startTask/{taskID}`

The whole URL for the GET is retirned by the POST in `Location` header

Body of post is a payload of text/plain format

Tests: RestAssured with Awaitility libs

## Quickstart
1) run 
2) do the needfulls


### Documentation
The `Readme.md` file



### Deployment
...




