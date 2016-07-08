package com.jj.pig;

import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.test.Util;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class BuildingAvgPigTest {
    static Cluster cluster;
    String pigScript = "src/main/pig/building-avg-age.pig";
    String[] args = new String[]{
            "buildingPath=/building.csv",
            "buildingAvgOut=."
    };

    PigTest pigTest;

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.getProperties().setProperty("pigunit.exectype", Util.getLocalTestMode().toString());
        cluster = PigTest.getCluster();
        cluster.copyFromLocalFile(
                new Path("src/main/resources/data/sensor-files/building.csv"),
                new Path("/building.csv")
        );
    }

    @Before
    public void setUp() throws Exception {
        pigTest = new PigTest(pigScript, args);
    }

    @Test
    public void calculatingBuildingAverageShouldProperlyStoreAverage() throws Exception {
        String[] building = new String[]{
                "1,m1,20,p1,c1",
                "2,m2,10,p2,c2",
                "3,m3,15,p3,c3",
                "4,m4,10,p4,c4",
                "5,m5,40,p5,c5"
        };

        String[] buildingAverage = new String[]{"19"};

        pigTest.assertOutput("building", building, "building_avg", buildingAverage);
    }
}
