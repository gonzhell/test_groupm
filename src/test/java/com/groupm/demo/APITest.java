package com.groupm.demo;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.qaprosoft.carina.core.foundation.api.http.HttpResponseStatusType;
import com.groupm.demo.api.GetCoordinatesByName;

import java.io.UnsupportedEncodingException;


public class APITest extends AbstractTest {


    @Parameters({"city", "coordinates_lat", "coordinates_lng"})
    @Test
    public void testCityCoordinates(String city, String coordinatesLat, String coordinatesLng) throws UnsupportedEncodingException, ParseException {
        GetCoordinatesByName getUsersMethods = new GetCoordinatesByName(city);
        getUsersMethods.expectResponseStatus(HttpResponseStatusType.OK_200);
        String rs = getUsersMethods.callAPI().asString();
        getUsersMethods.validateResponseAgainstJSONSchema("api/users/_get/rs.schema");
        Assert.assertTrue(rs.contains(String.format("\"lat\":%s", coordinatesLat)), "There is wrong lat coordinates provided");
        Assert.assertTrue(rs.contains(String.format("\"lng\":%s", coordinatesLat)), "There is wrong lng coordinates provided");
    }

}
