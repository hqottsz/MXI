package com.mxi.mx.testing.mock.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.crypto.parameter.ParameterEncrypter;
import com.mxi.mx.common.http.PageKey;
import com.mxi.mx.common.http.RequestData;
import com.mxi.mx.common.http.RequestDataFactory;
import com.mxi.mx.common.http.SessionData;
import com.mxi.mx.core.key.UserKey;


/**
 * Minial session implementation to facilitate testing without a running server.
 *
 * @author gpearson
 */
public class MockHttpSession implements HttpSession {

   private Map iAttributes = new HashMap();

   private Map<String, Object> iRequestData = new HashMap<String, Object>();

   private int iUserId = -1;

   private Map iValues = new HashMap();


   /**
    * Creates a new MockHttpSession object.
    *
    * @param aUserKey
    *           the user woh is (mock) requesting a page.
    */
   public MockHttpSession(UserKey aUserKey) {
      this( aUserKey.getId() );
   }


   /**
    * Creates a new MockHttpSession object.
    *
    * @param aUserId
    *           the user who is (mock) requesting a page.
    */
   @SuppressWarnings( "unchecked" )
   public MockHttpSession(int aUserId) {
      this.iUserId = aUserId;

      // Set up commonly required web session data.
      iAttributes.put( SessionData.USERID_SESSION_ATTRIBUTE, aUserId );
      iValues.put( SessionData.USERID_SESSION_ATTRIBUTE, aUserId );
      iRequestData = new HashMap<String, Object>();
      setAttribute( RequestData.MAP_SESSION_ATTRIBUTE, iRequestData );
      setAttribute( SessionData.ENCRYPTER_SESSION_ATTRIBUTE, new ParameterEncrypter() );
      setAttribute( "MAX_NUMBER_REGISTERED_OBJECTS", 1000 );

      // Set up needed SYSTEM parameter
      GlobalParametersStub.setInstance( new GlobalParametersStub( "SYSTEM" ) );

      GlobalParameters.getInstance( "SYSTEM" ).setInteger( "MAX_NUMBER_REGISTERED_OBJECTS", 1000 );
   }


   @Override
   public Object getAttribute( String aName ) {

      return iAttributes.get( aName );
   }


   @Override
   public Enumeration getAttributeNames() {

      return null;
   }


   @Override
   public long getCreationTime() {

      return 0;
   }


   @Override
   public String getId() {

      return null;
   }


   @Override
   public long getLastAccessedTime() {

      return 0;
   }


   @Override
   public int getMaxInactiveInterval() {

      return 0;
   }


   @Override
   public ServletContext getServletContext() {
      return new MockServletContext();
   }


   @Override
   @SuppressWarnings( "deprecation" )
   public javax.servlet.http.HttpSessionContext getSessionContext() {

      return null;
   }


   public int getUserId() {
      return iUserId;
   }


   @Override
   public Object getValue( String aName ) {

      return iValues.get( aName );
   }


   @Override
   public String[] getValueNames() {

      return null;
   }


   /**
    * No-op.
    */
   @Override
   public void invalidate() {
   }


   @Override
   public boolean isNew() {

      return false;
   }


   /**
    * Adds a name=>value mapping.
    *
    * @param aName
    *           name
    * @param aValue
    *           value
    */
   @Override
   @SuppressWarnings( "unchecked" )
   public void putValue( String aName, Object aValue ) {
      iValues.put( aName, aValue );
   }


   /**
    * Removes an attribute.
    *
    * @param aName
    *           attribute name.
    */
   @Override
   public void removeAttribute( String aName ) {
      iAttributes.remove( aName );
   }


   /**
    * No-op.
    *
    * @param aName
    *           removes a value from attributes.
    */
   @Override
   public void removeValue( String aName ) {
   }


   @Override
   @SuppressWarnings( "unchecked" )
   public void setAttribute( String aName, Object aValue ) {
      iAttributes.put( aName, aValue );
   }


   @Override
   public void setMaxInactiveInterval( int aInterval ) {
   }


   /**
    * Sets up mock "request data" (e.g. for UserSearchController.iUserSearchRd).
    *
    * @param aRequest
    *           the request for this session.
    * @param aSessionId
    *           the identifier for this request (e.g. USER_SEARCH.)
    * @param aKey
    *           the key within the request data.
    * @param aValue
    *           the value associated with this sessionId/key.
    */
   public void setRequestData( HttpServletRequest aRequest, String aSessionId, String aKey,
         Object aValue ) {

      RequestData lStoredData = ( RequestData ) iRequestData.get( aSessionId );
      if ( lStoredData == null ) {
         lStoredData =
               RequestDataFactory.getInstance( aRequest, aSessionId, new PageKey( aRequest ) );
         iRequestData.put( aSessionId, lStoredData );
      }

      lStoredData.setObject( aKey, aValue );
   }
}
