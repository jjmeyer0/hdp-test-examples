# HDP Test Examples

The goal of this project is to create unit test examples for common
HDP libraries. We will be focusing on Pig, Spark, HBase, Hive, and
Hadoop. It is the goal of this project to supply tests in Scala and 
Java.

## Running the Tests

Below is an example of how to run the current test suite using a
given profile. The profile hdp-2.4.2 is used in the example below. 

```bash
$ mvn clean test -P hdp-2.4.2
```


## Testing Libraries that Are Used

1. [JUnit](http://junit.org/junit4/)
2. [Mockito](http://mockito.org/)
3. [ScalaTest](http://www.scalatest.org/)
4. [Spark Testing Base](https://spark-packages.org/package/holdenk/spark-testing-base). Its source can be found [here](https://github.com/holdenk/spark-testing-base).
5. [PigUnit](http://pig.apache.org/docs/r0.15.0/test.html#pigunit). This link points to version 0.15.0 of pigunit.


## Test Data

Using sensor data from this [tutorial](http://hortonworks.com/hadoop-tutorial/how-to-analyze-machine-and-sensor-data/)