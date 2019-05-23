package com.mxi.mx.testing.mock.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;


/**
 * Minimal mock servlet request to facilitate testing without having a server running.
 *
 * @author gpearson
 */
public class MockHttpServletRequest implements HttpServletRequest {

   private Map<String, Object> iAttributes = new HashMap<String, Object>();

   private Map<String, String[]> iParameterMap = new HashMap<String, String[]>();

   private HttpSession iSession;


   /**
    * Creates a new MockHttpServletRequest object.
    *
    * @param aSession
    *           the session associated with this request.
    */
   public MockHttpServletRequest(MockHttpSession aSession) {
      this.iSession = aSession;

      int lUserId = aSession.getUserId();

      // Set up commonly needed web parameter holders and information.
      GlobalParametersStub.setInstance( new GlobalParametersStub( "LOGIC" ) );
      GlobalParametersStub.setInstance( new GlobalParametersStub( "MXCOMMONWEB" ) );
      GlobalParametersStub.setInstance( new GlobalParametersStub( "MXWEB" ) );

      GlobalParameters.getInstance( "MXCOMMONWEB" ).setInteger( "MAXIMUM_POST_DATA_SIZE", 1024 );

      UserParametersStub lLogicUserParametersStub = new UserParametersStub( lUserId, "LOGIC" );
      UserParameters.setInstance( lUserId, "LOGIC", lLogicUserParametersStub );

      UserParametersStub lWebUserParametersStub = new UserParametersStub( lUserId, "MXWEB" );
      UserParameters.setInstance( lUserId, "MXWEB", lWebUserParametersStub );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean authenticate( HttpServletResponse aArg0 ) throws IOException, ServletException {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AsyncContext getAsyncContext() {
      return null;
   }


   @Override
   public Object getAttribute( String aName ) {

      return iAttributes.get( aName );
   }


   @Override
   public Enumeration<String> getAttributeNames() {

      return null;
   }


   @Override
   public String getAuthType() {

      return null;
   }


   @Override
   public String getCharacterEncoding() {

      return null;
   }


   @Override
   public int getContentLength() {

      return 0;
   }


   @Override
   public String getContentType() {

      return null;
   }


   @Override
   public String getContextPath() {

      return null;
   }


   @Override
   public Cookie[] getCookies() {

      return null;
   }


   @Override
   public long getDateHeader( String aName ) {

      return 0;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public DispatcherType getDispatcherType() {
      return null;
   }


   @Override
   public String getHeader( String aName ) {

      return null;
   }


   @Override
   public Enumeration<String> getHeaderNames() {

      return null;
   }


   @Override
   public Enumeration<String> getHeaders( String aName ) {

      return null;
   }


   @Override
   public ServletInputStream getInputStream() throws IOException {

      return null;
   }


   @Override
   public int getIntHeader( String aName ) {

      return 0;
   }


   @Override
   public String getLocalAddr() {

      return null;
   }


   @Override
   public Locale getLocale() {

      return null;
   }


   @Override
   public Enumeration<Locale> getLocales() {

      return null;
   }


   @Override
   public String getLocalName() {

      return null;
   }


   @Override
   public int getLocalPort() {

      return 0;
   }


   @Override
   public String getMethod() {
      return "get";
   }


   @Override
   public String getParameter( String aName ) {
      if ( iParameterMap.containsKey( aName ) ) {
         return iParameterMap.get( aName )[0].toString();
      }

      return null;
   }


   @Override
   public Map<String, String[]> getParameterMap() {
      return iParameterMap;
   }


   @Override
   public Enumeration<String> getParameterNames() {
      return Collections.enumeration( iParameterMap.keySet() );
   }


   @Override
   public String[] getParameterValues( String aName ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Part getPart( String aArg0 ) throws IOException, ServletException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Collection<Part> getParts() throws IOException, ServletException {
      return null;
   }


   @Override
   public String getPathInfo() {

      return null;
   }


   @Override
   public String getPathTranslated() {

      return null;
   }


   @Override
   public String getProtocol() {

      return null;
   }


   @Override
   public String getQueryString() {

      return null;
   }


   @Override
   public BufferedReader getReader() throws IOException {

      return null;
   }


   @Override
   public String getRealPath( String aPath ) {

      return null;
   }


   @Override
   public String getRemoteAddr() {

      return null;
   }


   @Override
   public String getRemoteHost() {

      return null;
   }


   @Override
   public int getRemotePort() {

      return 0;
   }


   @Override
   public String getRemoteUser() {

      return null;
   }


   @Override
   public RequestDispatcher getRequestDispatcher( String aPath ) {

      return null;
   }


   @Override
   public String getRequestedSessionId() {

      return null;
   }


   @Override
   public String getRequestURI() {
      return "http://localhost/foo";
   }


   @Override
   public StringBuffer getRequestURL() {

      return null;
   }


   @Override
   public String getScheme() {

      return null;
   }


   @Override
   public String getServerName() {

      return null;
   }


   @Override
   public int getServerPort() {

      return 0;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ServletContext getServletContext() {
      return null;
   }


   @Override
   public String getServletPath() {

      return null;
   }


   @Override
   public HttpSession getSession() {

      return iSession;
   }


   @Override
   public HttpSession getSession( boolean aCreate ) {

      return iSession;
   }


   @Override
   public Principal getUserPrincipal() {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAsyncStarted() {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAsyncSupported() {
      return false;
   }


   @Override
   public boolean isRequestedSessionIdFromCookie() {

      return false;
   }


   @Override
   public boolean isRequestedSessionIdFromURL() {

      return false;
   }


   @Override
   public boolean isRequestedSessionIdFromUrl() {

      return false;
   }


   @Override
   public boolean isRequestedSessionIdValid() {

      return false;
   }


   @Override
   public boolean isSecure() {

      return false;
   }


   @Override
   public boolean isUserInRole( String aRole ) {

      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void login( String aArg0, String aArg1 ) throws ServletException {
      // No-op
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void logout() throws ServletException {
      // No-op
   }


   /**
    * No-op
    *
    * @param aName
    *           attribute name.
    */
   @Override
   public void removeAttribute( String aName ) {
      // No-op
   }


   @Override
   public void setAttribute( String aKey, Object aValue ) {
      iAttributes.put( aKey, aValue );
   }


   @Override
   public void setCharacterEncoding( String aEnv ) throws UnsupportedEncodingException {
      // No-op
   }


   public String setParameter( String aKey, Object aValue ) {
      String[] lNewValue = null;
      if ( ( aValue != null ) && ( aValue instanceof String[] ) ) {
         lNewValue = ( String[] ) aValue;
      } else if ( aValue != null ) {
         lNewValue = new String[] { aValue.toString() };
      }

      iParameterMap.put( aKey, lNewValue );

      return Arrays.toString( lNewValue );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AsyncContext startAsync() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AsyncContext startAsync( ServletRequest aArg0, ServletResponse aArg1 )
         throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public long getContentLengthLong() {
      // TODO Auto-generated method stub
      return 0;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public String changeSessionId() {
      // TODO Auto-generated method stub
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public <T extends HttpUpgradeHandler> T upgrade( Class<T> aArg0 )
         throws IOException, ServletException {
      // TODO Auto-generated method stub
      return null;
   }
}
