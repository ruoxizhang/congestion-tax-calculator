# Congestion Tax Calculator

This is a demo project for calculating congestion fees.

## Design

When design the code, I tried focus more on making the code more readable, clean and clear. So there
might be some intermediate variables created to separate the functions.

I choose to use MongoDB to save toll fee rules, reason of that is the data is not transactional and
suit quite well for MongoDB to handle it as document data.

For testing, considering the time limitation, I only composed some integration test with test
containers to run MongoDB there.

## How to test it in dev mode

1. run `docker-compose up` in the project folder
2. run the project with default `profile` Intellij
3. use Postman to make a `Post` request to `http://localhost:8080/api/tollFee` with data `{"dates":["2013-01-14T21:00:00","2013-01-15T21:00:00","2013-02-07T06:23:27","2013-02-07T15:27:00","2013-02-08T06:27:00","2013-02-08T06:20:27","2013-02-08T14:35:00","2013-02-08T15:29:00","2013-02-08T15:47:00","2013-02-08T16:01:00","2013-02-08T16:48:00","2013-02-08T17:49:00","2013-02-08T18:29:00","2013-02-08T18:35:00","2013-03-26T14:25:00","2013-03-28T14:07:27"],"vehicleType":"Car","tollFeeCity":"Gothenburg"}`

## Questions

1. Should the free pass be seen as a valid pass when calculating toll fees for a time period of 60
   minutes. The implementation of this code ignores the free pass and see it as invalid start point
   of a 60-minutes-interval. But if that is not the case, should just remove the filtering part.
2. There are Vehicle type Motorcycle and data model Motorbike in the demo code, wondering what is
   the difference between those.

## Problem of the demo java code

1. do not update intervalStart
2. do not calculate toll fee with regard of days
3. do not handle the case that the start time is a free pass.(otherwise might be a problem of my
   understanding)

## To do

1. More unit test
2. Documentation
