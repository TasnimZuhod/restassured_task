package iqvia_task;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;

/**
 * API calls
 *
 */

public class Countries {

  private CommandAction cmdAction = null;
  private Command cmd = null;
  
  String baseUrl = "https://restcountries.eu/rest/v2/";
  
  public Countries() {
    cmdAction = new CommandAction();
    cmd = new Command();
  }
  
  /**
   * get all countries.
   * @throws Exception ex
   */
  public Response getCountries() throws Exception {
    
    cmd.url = baseUrl + "all?fields=name;capital;currencies;latlng";
    
    Response restResult = cmdAction.get(cmd);
    
    return restResult;
  }
  
  /**
   * get one country based on the passed capital name.
   * @throws Exception ex
   */
  public Response getCountry(String capitalName) throws Exception {
    
    cmd.url = baseUrl + "capital/{capital}?fields=name;capital;currencies;latlng;regionalBlocs";
    cmd.paramName = "capital";
    cmd.paramValue = capitalName;
    
    Response restResult = cmdAction.getWithParams(cmd);
    
    return restResult;
  }
  
  public Object mapResponseBody(Response restResult) throws JsonParseException, JsonMappingException, IOException {
    
    String bodyResponse = restResult.body().asString();
    ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    
    Object restResponse = null;

    if(restResult.statusCode() == 200) {
      restResponse = mapper.readValue(bodyResponse, new TypeReference<Country[]>() {});
    } else if(restResult.statusCode() == 404) {
      restResponse = mapper.readValue(bodyResponse, new TypeReference<Message>() {});
    }
    
    return restResponse;
  }
  
}
