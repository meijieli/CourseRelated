# Database Applications

Requirements
---
Write the following functions:

* find_mysql_by_id(id)

>>This function connects to MySQL and returns the tuple from the Master (or MasterSmall) table with playerID equal to the input value.

* find_redis_by_id(id)

>>This does the same for Redis.

* add_to_redis(id, data)

>>Given a JSON structure, this procedure creates an HSET with key players:id and has entries for the elements in the JSON data. The JSON data will have come from the result of find_mysql_by_id(id).

* find_by_id(id)
>>This function implements the following logic:
>>Call find_redis_by_id(id): If the result is found, return the result.
>>If the result is not found in step 1,
>>Call find_mysql_by_id(id)
>>Add the result from the MySQL to Redis using add_to_redis(id, data).
>>Return the result.

Files
---

**Project5.java**: The java code that implements the four functions mentioned above

**hw5.sql**: The database that is a smaller, simpler database than the Lahman 2016 baseball database
