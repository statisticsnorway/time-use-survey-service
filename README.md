# Timeuse survey: PWA backend service

This application is a mimimalist approach to a simple backend service providing endpoints for the PWA used in the national timeuse survey.

## Boot-up
The application can be run locally as a Docker image, as a Jar, or by running TimeuseSurveyServiceApplication.class in your favorite IDE.
When running locally, all URLs are exposed at http://localhost:8080.

## Features
These features are organized as packages which may easily be migrated to individual microservices when necessary.

### Activitycodes
Activitycodes are internationally recognized activity codes and descriptions. These are stored in a database and exposed in various endpoints:
- Search: `/no.ssb.timeuse.surveyservice.activity/v1/search?query=xyz`. Returns a list of ActivityResponse-objects
- Fetch single: `/no.ssb.timeuse.surveyservice.activity/v1/{description}`. Returns one ActivityResponse-object.

There is also an MVP website interface to manage Activity-codes exposed at `/admin`


##Persistent database with docker
`command:` 
- mvn clean install
- docker-compose up
