# Mountain Bike Race Tracking System


``
This application is designed to track mountain bike races, riders, and race results. The system also integrates with weather services to fetch real-time weather conditions for race locations. It allows users to create and manage races, riders, and results while also generating useful reports such as top 3 riders, riders who did not finish, and those who did not participate in the race.
``

## Prerequisites
Before you can run the project, ensure you have the following installed on your local machine:
```aiignore
Java 17+
Maven 3.8+
H2 database
Git
IDE (IntelliJ IDEA, Eclipse, etc.)
```
  
## Installation

```aiignore
git clone https://github.com/ollysun/racing
cd racing

To install the dependency
mvn clean install

To run the test:
mvn test
```
## Running the Application

```aiignore
Start the application

mvn spring-boot:run

Swagger documentation

http://localhost:9090/swagger-ui/index.html#/

```

## API Documentation

### Create a new Rider
 ```
 POST http://localhost:9090/riders
{
  "name": "John Doe",
  "email": "ollysun@gmail.com"
}
 ```
### Get riders
```aiignore

GET  http://localhost:9090/riders
```

### Get rider by ID
```aiignore

GET  http://localhost:9090/riders/{id}
```
## Assumption
```aiignore
l believe every race must have an estimated minimum duration. In order to meet the 
requirement  for either finish or non finish race. It either meet it or higher . i.e finish time equal to 
race duration. Rider can have issue with the bike on the way so he finished with less time . 
He could be judge to not conclude the race
```
### Create new race
```aiignore
 POST http://localhost:9090/races

{
  "name": "lagos beanch",
  "startTime": "2024-10-08T10:34:38.152Z",
  "location": "lagos",
  "raceDuration": 5000
}
```

### Get all races
```aiignore

GET  http://localhost:9090/races
```

### Get race by Id
```aiignore

GET  http://localhost:9090/races/{id}
```

### Create new race result

```aiignore

 POST http://localhost:9090/race-results

{
  "raceId": 1,
  "riderId": 2,
  "finishTime": 5400,
}
```

### Get top 3 riders for a race:
```aiignore
GET http://localhost:9090/race-results/top3/{raceId}
```

### Get riders who did not finish a race:
```aiignore

GET  http://localhost:9090/race-results/dnf/{raceId}
```

### Get riders who did not participate:
```aiignore

GET  http://localhost:9090/race-results/not-participated/{raceId}
```

### Get current weather for a location:
```aiignore
GET  http://localhost:9090/weather/lagos
```