package com.mxi.am.helper.api;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.path.PathBuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Generic calls to AM APIs
 *
 */
public class GenericAmApiCalls {

   private static final String AMAPI = "/amapi/";


   public static Response create( int statusCode, Credentials security, Object entity, String path,
         String contentType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).body( entity ).expect()
            .statusCode( statusCode ).when().post( AMAPI + path );
      return response;
   }


   public static Response update( int statusCode, Credentials security, Object entity, String path,
         String id, String contentType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).body( entity ).expect()
            .statusCode( statusCode ).when().put( AMAPI + path + "/" + id );
      return response;
   }


   public static Response search( int statusCode, Credentials security, String path,
         Map<String, String> searchParameters, String contentType )
         throws JsonParseException, JsonMappingException, IOException {

      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .queryParams( searchParameters ).accept( contentType ).contentType( contentType )
            .expect().statusCode( statusCode ).when().get( AMAPI + path );
      return response;
   }


   public static Response get( int statusCode, Credentials security, String path, String id,
         String contentType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( AMAPI + path + "/" + id );
      return response;
   }


   public static Response create( PathBuilder pathBuilder, Object entity, int statusCode,
         Credentials security, String contentType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + pathBuilder.post().build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).body( entity ).expect()
            .statusCode( statusCode ).when().post( path );
      return response;
   }


   public static Response update( PathBuilder pathBuilder, String id, Object entity,
         int statusCode, Credentials security, String contentType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + pathBuilder.put( id ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).body( entity ).expect()
            .statusCode( statusCode ).when().put( path );
      return response;
   }


   @SuppressWarnings( "unchecked" )
   public static Response search( PathBuilder pathBuilder, Object searchParameters, int statusCode,
         Credentials security, String contentType )
         throws JsonParseException, JsonMappingException, IOException {

      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + pathBuilder.search().searchBy( searchParameters ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( path );
      return response;
   }


   public static Response get( PathBuilder pathBuilder, String id, int statusCode, Credentials security,
         String contentType ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      String path = AMAPI + pathBuilder.get( id ).build();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( contentType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( path );
      return response;
   }

}
