package com.mxi.mx.common.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;


public class UserParametersStubTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   // The parameter type is not considered relevant to this test (other than it has to be as
   // supported type), therefore we will use the type LOGIC.
   private final static String PARM_TYPE = com.mxi.mx.common.config.ParmTypeEnum.LOGIC.toString();

   // The following are 0-level user parameters that will be used to test with (even though
   // utl_user_parm is not accessed in any way).
   private final static String LOGIC_BOOLEAN_PARM_NAME = "ENFORCE_SIGNATURES";
   private final static String LOGIC_STRING_PARM_NAME = "RELEASE_AOG_FAULT";
   private final static String LOGIC_INTEGER_PARM_NAME = "RELEASE_NOTIFY_INTERVAL";

   private final static int USER_ID = 1;


   // TEST getBoolean()

   @Test
   public void whenBooleanNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getBoolean( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test
   public void whenBooleanSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.setBoolean( LOGIC_BOOLEAN_PARM_NAME, Boolean.FALSE );
      assertEquals( Boolean.FALSE, lStub.getBoolean( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test
   public void whenNoBooleanParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getBoolean( "non-existent param name" ) );
   }


   // TEST getBool()

   @Test( expected = NullPointerException.class )
   public void whenBoolNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      // If parameter is not set then cannot return null as primitive boolean, so throw NPE.
      lStub.getBool( LOGIC_BOOLEAN_PARM_NAME );
   }


   @Test
   public void whenBoolSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.setBoolean( LOGIC_BOOLEAN_PARM_NAME, true );
      assertEquals( true, lStub.getBool( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test( expected = NullPointerException.class )
   public void whenNoBoolParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      // If parameter is not set then cannot return null as primitive boolean, so throw NPE.
      lStub.getBool( "non-existent param name" );
   }


   // TEST getString()

   @Test
   public void whenStringNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getString( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenStringSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.setString( LOGIC_STRING_PARM_NAME, "INFO" );
      assertEquals( "INFO", lStub.getString( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenNoStringParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getString( "non-existent param name" ) );
   }


   // TEST getInt()

   @Test( expected = NullPointerException.class )
   public void whenIntNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      // If parameter is not set then cannot return null as primitive int, so throw NPE.
      lStub.getInt( LOGIC_INTEGER_PARM_NAME );
   }


   @Test
   public void whenIntSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      assertEquals( 100, lStub.getInt( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test( expected = NullPointerException.class )
   public void whenNoIntParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      // If parameter is not set then cannot return null as primitive int, so throw NPE.
      lStub.getInt( "non-existent param name" );
   }


   // TEST getInteger()

   @Test
   public void whenIntegerNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getInteger( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenIntegerSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      assertEquals( new Integer( 100 ), lStub.getInteger( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenNoIntegerParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getInteger( "non-existent param name" ) );
   }


   // TEST getBigDecimal()

   @Test
   public void whenBigDecimalNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getBigDecimal( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenBigDecimalSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      // Note there is no way to set a BigDecimal, can only get a BigDecimal.
      lStub.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      assertEquals( new BigDecimal( 100 ), lStub.getBigDecimal( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenNoBigDecimalParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getBigDecimal( "non-existent param name" ) );
   }


   // TEST getFloat()

   @Test
   public void whenFloatNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getFloat( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenFloatSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      // Note there is no way to set a Float, can only get a Float.
      lStub.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      assertEquals( new Float( 100 ), lStub.getFloat( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenNoFloatParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getFloat( "non-existent param name" ) );
   }


   // TEST getProperty()

   @Test
   public void whenPropertyNotSet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getProperty( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenPropertySet() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.setProperty( LOGIC_STRING_PARM_NAME, "WARN" );
      assertEquals( "WARN", lStub.getProperty( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenNoPropertyParmName() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      assertNull( lStub.getProperty( "non-existent param name" ) );
   }


   // TEST storeParameter()

   @Test
   public void whenStoreExistingParameter() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.storeParameter( LOGIC_STRING_PARM_NAME, "WARN" );
      assertEquals( "WARN", lStub.getProperty( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenStoreNewParameter() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.storeParameter( "hello", "world" );
      assertEquals( "world", lStub.getString( "hello" ) );
   }


   @Test
   public void whenStoreNullParameter() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.storeParameter( null, "WARN" );
      assertEquals( "WARN", lStub.getProperty( null ) );
   }


   // TEST propertyNames(), getProperties(), exists(), remove() which are all not supported.

   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingPropertyNames() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.propertyNames();
   }


   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingGetProperties() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.getProperties();
   }


   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingExists() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.exists( LOGIC_STRING_PARM_NAME, PARM_TYPE );
   }


   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingRemove() {
      UserParametersStub lStub = new UserParametersStub( USER_ID, PARM_TYPE );
      lStub.remove( LOGIC_STRING_PARM_NAME );
   }

}
