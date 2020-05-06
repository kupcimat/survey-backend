# SurWay
SurWay is a distributed application for sending survey messages to registered users and processing their results. It uses a simple SMS system to interact with users and collect their responses. The application then publishes the responses using extensible set of cloud functions.

## How to run the application
First you need to build Docker images for application components:
```shell script
./gradlew worker:jibDockerBuild
./gradlew restapi:jibDockerBuild
```
Then you can use `docker-compose` to run the application locally with all required dependencies:
```shell script
docker-compose up
```
When you finish, you can cleanup resources using:
```shell script
docker-compose down
```

## Architecture
Application consists of `restapi` and `worker` nodes. They communicate using messages in `RabbitMQ`. `MongoDB` is used to store the application state.
```
                   +--------+
                   | MongoDB|
    +------------->+        +<-------------+
    |              |        |              |
    |              +--------+              |
    |                                      |
+---+---+                              +---+--+
|restapi|     +------------------+     |worker|
|       +---->+     RabbitMQ     +---->+      |
+-------+     | +--------------+ |     +------+
              |                  |
+-------+     | +--------------+ |     +------+
|restapi+---->+                  +---->+worker|
|       |     +------------------+     |      |
+-------+                              +------+
```

### User task lifecycle
User task represents user survey request, which goes through multiple stages after it's created:
```
 create         send          receive               run cloud
 task   +-----+ sms  +------+ response +----------+ function  +------+
+------>+ NEW +----->+ SENT +--------->+ RECEIVED +---------->+ DONE |
        +-----+      +------+          +----------+           +------+
```

### restapi
Public facing REST API with JSON payload. It contains following endpoints for accessing user tasks:
```
> GET /api/userTasks
< 200 OK
```
```json
{
  "userTaskList": {
    "items": [],
    "paging": {
      "nextUri": null
    }
  }
}
```
- returns the list of all registered user tasks  

```
> GET /api/userTasks/<task-id>
< 200 OK
< 404 Not Found (if the task doesn't exist)
```
```json
{
  "userTask": {
    "functionId": "EMAIL",
    "phone": "+420123456789",
    "status": "DONE"
  }
}
```
- returns single task or 404 if the task doesn't exist

```
> POST /api/userTasks
```
```json
{
  "userTask": {
    "phone": "+420123456789",
    "functionId": "EMAIL"
  }
}
```
```
< 201 Created
< Location: /api/userTasks/<task-id>
```
```json
{
  "userTask": {
    "functionId": "EMAIL",
    "phone": "+420123456789",
    "status": "NEW"
  }
}
```
- creates a new user task
- schedules SMS using provided phone and cloud function

```
> POST /api/userTasks/<task-id>/message
```
```json
{
  "userTaskMessage": {
    "value": "response message"
  }
}
```
```
< 202 Accepted
< 404 Not Found (if the task doesn't exist)
```
```json
{
  "userTask": {
    "functionId": "EMAIL",
    "phone": "+420123456789",
    "status": "RECEIVED"
  }
}
```
- saves message response for user task
- schedules run of a configured cloud function
- with modifications could be used in a webhook of 3rd party SMS service (e.g. Twilio)

### worker
Executor of async tasks - sending SMS and running cloud functions. Easily extensible by defining and registering new cloud functions.

## Missing / TODOs
- Frontend, simple JavaScript SPA having form to register new user tasks (phone & cloud function). Communicating with the backend using the public REST API.
