building = LOAD '$buildingPath' USING PigStorage(',') AS (buildingid:int, buildingmgr:chararray, buildingage:double, hvacproduct:chararray, country:chararray);
building_group = GROUP building ALL;
building_avg = FOREACH building_group GENERATE COUNT(building.buildingage) AS average_age:double;

STORE building_avg INTO '$buildingAvgOut' USING PigStorage(',');
