package com.mxi.mx.testing.mock.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;


/**
 * Very basic servlet context for mocking calls to controllers.
 *
 * @author gpearson
 */
public class MockServletContext implements ServletContext {

   /**
    * {@inheritDoc}
    */
   @Override
   public Dynamic addFilter( String aArg0, String aArg1 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Dynamic addFilter( String aArg0, Filter aArg1 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Dynamic addFilter( String aArg0, Class<? extends Filter> aArg1 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addListener( String aArg0 ) {
      // No-op
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public <T extends EventListener> void addListener( T aArg0 ) {
      // No-op
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addListener( Class<? extends EventListener> aArg0 ) {
      // No-op
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public javax.servlet.ServletRegistration.Dynamic addServlet( String aArg0, String aArg1 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public javax.servlet.ServletRegistration.Dynamic addServlet( String aArg0, Servlet aArg1 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public javax.servlet.ServletRegistration.Dynamic addServlet( String aArg0,
         Class<? extends Servlet> aArg1 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public <T extends Filter> T createFilter( Class<T> aArg0 ) throws ServletException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public <T extends EventListener> T createListener( Class<T> aArg0 ) throws ServletException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public <T extends Servlet> T createServlet( Class<T> aArg0 ) throws ServletException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void declareRoles( String... aArg0 ) {
      // No-op
   }


   @Override
   public Object getAttribute( String aName ) {

      // Reference to SessionData.REGISTERED_OBJECTS_ATTNAME
      if ( "sRegisteredObjects".equals( aName ) ) {
         return new HashMap<Object, Object>();
      }

      return null;
   }


   @Override
   public Enumeration<String> getAttributeNames() {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ClassLoader getClassLoader() {
      return null;
   }


   @Override
   public ServletContext getContext( String aUripath ) {

      return null;
   }


   /**
    * @see javax.servlet.ServletContext#getContextPath()
    */
   @Override
   public String getContextPath() {
      return "";
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int getEffectiveMajorVersion() {
      return 0;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int getEffectiveMinorVersion() {
      return 0;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public FilterRegistration getFilterRegistration( String aArg0 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
      return null;
   }


   @Override
   public String getInitParameter( String aName ) {

      return null;
   }


   @Override
   public Enumeration<String> getInitParameterNames() {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public JspConfigDescriptor getJspConfigDescriptor() {
      return null;
   }


   @Override
   public int getMajorVersion() {

      return 0;
   }


   @Override
   public String getMimeType( String aFile ) {

      return null;
   }


   @Override
   public int getMinorVersion() {

      return 0;
   }


   @Override
   public RequestDispatcher getNamedDispatcher( String aName ) {

      return null;
   }


   @Override
   public String getRealPath( String aPath ) {

      return null;
   }


   @Override
   public RequestDispatcher getRequestDispatcher( String aPath ) {

      return null;
   }


   @Override
   public URL getResource( String aPath ) throws MalformedURLException {

      return null;
   }


   @Override
   public InputStream getResourceAsStream( String aPath ) {

      return null;
   }


   @Override
   public Set<String> getResourcePaths( String aPath ) {

      return null;
   }


   @Override
   public String getServerInfo() {

      return null;
   }


   @Override
   public Servlet getServlet( String aName ) throws ServletException {

      return null;
   }


   @Override
   public String getServletContextName() {

      return null;
   }


   @Override
   public Enumeration<String> getServletNames() {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ServletRegistration getServletRegistration( String aArg0 ) {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, ? extends ServletRegistration> getServletRegistrations() {
      return null;
   }


   @Override
   public Enumeration<Servlet> getServlets() {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public SessionCookieConfig getSessionCookieConfig() {
      return null;
   }


   /**
    * Swallows a log message.
    *
    * @param aMsg
    *           log message.
    */
   @Override
   public void log( String aMsg ) {
      // No-op
   }


   /**
    * Swallows a logged exception.
    *
    * @param aException
    *           log exception
    * @param aMsg
    *           log message
    */
   @Override
   public void log( Exception aException, String aMsg ) {
      // No-op
   }


   /**
    * Swallows a logged exception.
    *
    * @param aMessage
    *           log message
    * @param aThrowable
    *           log exception
    */
   @Override
   public void log( String aMessage, Throwable aThrowable ) {
      // No-op
   }


   /**
    * No-op.
    *
    * @param aName
    *           attribute name.
    */
   @Override
   public void removeAttribute( String aName ) {
      // No-op
   }


   @Override
   public void setAttribute( String aName, Object aObject ) {
      // No-op
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean setInitParameter( String aArg0, String aArg1 ) {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setSessionTrackingModes( Set<SessionTrackingMode> aArg0 ) {
      // No-op
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public String getVirtualServerName() {
      return null;
   }
}
