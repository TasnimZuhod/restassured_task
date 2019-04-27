package iqvia_task;

import java.util.ArrayList;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import io.restassured.response.Response;

/**
 * Positive test: get country using randomly selected capital name
 *
 */

public class PositiveTest {
  
  private Countries countriesApi = null;
  private Response response = null;
  private JSONObject countryJson = null;
	
  @BeforeTest
  public void beforeTest() throws Exception {
    countriesApi = new Countries();
    /* get all countries - Countries API */
    response = countriesApi.getCountries();
    /* parse the response to JSON array then select random country as JSON object */
    JSONArray countriesJsonArr = new JSONArray(response.asString());
    int countryIndex = new Random().nextInt(countriesJsonArr.length());
    countryJson = countriesJsonArr.getJSONObject(countryIndex);
  }
	
  @Test
  public void getCountry() throws Exception {
    
    String capitalName = countryJson.getString("capital");
    int expectedStatus = 200;
    int firstCapital = 0;
    
    /* get country based on the selected capital name */
    response = countriesApi.getCountry(capitalName);
    
    
    /* validate the status code */
    Assert.assertEquals(response.statusCode(), expectedStatus);
    
    
    /* validate the response body schema */
    Country[] responseCountry = {};
    
    try {
      responseCountry = (Country[]) countriesApi.mapResponseBody(response);
    }
    catch(Exception ex) {
      throw new Exception("Failed to map the Country response", ex);
    }
    
    Assert.assertTrue(responseCountry.length != 0);
    Assert.assertEquals(responseCountry[firstCapital].capital, capitalName);
    
    
    /* validate the currency code CountryApi - CapitalApi */

    Object capitalApiCurrencyCode = getCurrencyCodes(
        new JSONArray(response.asString()).getJSONObject(firstCapital));

    Object countryApiCurrencyCode = getCurrencyCodes(countryJson);
    
    Assert.assertEquals(capitalApiCurrencyCode, countryApiCurrencyCode);
    
  }
  
  /**
   * get all currency codes for specific capital.
   * (some countries has more than one code)
   * @throws JSONException ex
   */
  private Object getCurrencyCodes(JSONObject capital) throws JSONException {
    
    JSONArray currencies = capital.getJSONArray("currencies");
    
    ArrayList<String> currencyCodes = new ArrayList<String>();
    
    for (int i = 0; i < currencies.length(); i++)
      if(currencies.getJSONObject(i).has("code"))
        currencyCodes.add(currencies.getJSONObject(i).getString("code"));
    
    if(currencyCodes.size() == 1)
      return currencyCodes.get(0);
        
    return currencyCodes;
  }
  
}
