Task is in task.zip password is "task" (to avoid indexation by search engines)

Install:
mvn install

Startup:
cd target
java - jar meter-data-server-0.0.1-SNAPSHOT.jar

Usage:
POST http://localhost:8080/100/submitMeterData
{
  "coldWater" : 100501,
  "hotWater" : 100500,
  "gas" : 100502
}
Will submit data for user with id 100
Data values must be positive. Values less than previously submitted are considered invalid, too.

GET http://localhost:8080/100/meterData
Will return stored data for user with id 100

Values are cached, caches are evicted when new data is saved