package iqvia_task;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;

/**
 * Negative test: get country using non-exist capital name
 *
 */

public class NegativeTest {
  
  private Countries countriesApi = null;
	
  @BeforeTest
  public void beforeTest() throws Exception {
    countriesApi = new Countries();
  }
	
  @Test
  public void getCountryOfNonExistCapital() throws Exception {
    
    int expectedStatus = 404;
    String invalidCapitalName = "nonExistCapitalName";
    
    /* get country with non-exist capital name */
    Response response = countriesApi.getCountry(invalidCapitalName);

    /* validate the status code */
    Assert.assertEquals(response.statusCode(), expectedStatus);
    
    
    /* validate the response body */
    Message responseMessage = null;
    
    try {
      responseMessage = (Message) countriesApi.mapResponseBody(response);
    }
    catch(Exception ex) {
      throw new Exception("Failed to map the Message response", ex);
    }
    
    Assert.assertEquals(responseMessage.status, expectedStatus);
    Assert.assertEquals(responseMessage.message, "Not Found");

  }
  
}
