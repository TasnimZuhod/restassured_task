package iqvia_task;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * all http requests (rest-assured calls)
 *
 */

public class CommandAction {

	public Response get(Command cmd) throws Exception {

		Response result = null;
		
	    try {
	      
	      result = RestAssured.given()
			.expect()
			.when()
			.get(cmd.url);
			
		} catch (Exception ex) {
			throw new Exception("GET command failed " + ex.getMessage());
		}
	    
		return result;
	}
	
	public Response getWithParams(Command cmd) throws Exception {

    Response result = null;
    
      try {
        
        result = RestAssured.given()
      .pathParam(cmd.paramName, cmd.paramValue)
      .expect()
      .when()
      .get(cmd.url);
      
    } catch (Exception ex) {
      throw new Exception("GET command failed " + ex.getMessage());
    }
      
    return result;
  }
	
}
