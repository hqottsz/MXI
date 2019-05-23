package com.mxi.mx.common.config;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;


public class UserParametersFakeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   // The parameter type is not considered relevant to this test (other than it has to be as
   // supported type), therefore we will use the type LOGIC.
   private final static String PARM_TYPE = com.mxi.mx.common.config.ParmTypeEnum.LOGIC.toString();

   // The following are 0-level user parameters that will be used to test with.
   private final static String LOGIC_BOOLEAN_PARM_NAME = "ENFORCE_SIGNATURES";
   private final static boolean LOGIC_BOOLEAN_PARM_DEFAULT_VALUE = true;

   private final static String LOGIC_STRING_PARM_NAME = "RELEASE_AOG_FAULT";
   private final static String LOGIC_STRING_PARM_DEFAULT_VALUE = "ERROR";

   private final static String LOGIC_INTEGER_PARM_NAME = "RELEASE_NOTIFY_INTERVAL";
   private final static int LOGIC_INTEGER_PARM_DEFAULT_VALUE = 5;

   private final static int USER_ID = 1;


   // TEST getBoolean()

   @Test
   public void whenBooleanNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( LOGIC_BOOLEAN_PARM_DEFAULT_VALUE,
            lFake.getBoolean( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test
   public void whenBooleanSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.setBoolean( LOGIC_BOOLEAN_PARM_NAME, !LOGIC_BOOLEAN_PARM_DEFAULT_VALUE );
      Assert.assertEquals( !LOGIC_BOOLEAN_PARM_DEFAULT_VALUE,
            lFake.getBoolean( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test
   public void whenNoBooleanParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertNull( lFake.getBoolean( "non-existent param name" ) );
   }


   // TEST getBool()

   @Test
   public void whenBoolNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( LOGIC_BOOLEAN_PARM_DEFAULT_VALUE,
            lFake.getBool( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test
   public void whenBoolSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.setBoolean( LOGIC_BOOLEAN_PARM_NAME, !LOGIC_BOOLEAN_PARM_DEFAULT_VALUE );
      Assert.assertEquals( !LOGIC_BOOLEAN_PARM_DEFAULT_VALUE,
            lFake.getBool( LOGIC_BOOLEAN_PARM_NAME ) );
   }


   @Test( expected = NullPointerException.class )
   public void whenNoBoolParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      // If parameter is not set then cannot return null as primitive boolean, so throw NPE.
      lFake.getBool( "non-existent param name" );
   }


   // TEST getString()

   @Test
   public void whenStringNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( LOGIC_STRING_PARM_DEFAULT_VALUE,
            lFake.getString( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenStringSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.setString( LOGIC_STRING_PARM_NAME, "INFO" );
      Assert.assertEquals( "INFO", lFake.getString( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenNoStringParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertNull( lFake.getString( "non-existent param name" ) );
   }


   // TEST getInt()

   @Test
   public void whenIntNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( LOGIC_INTEGER_PARM_DEFAULT_VALUE,
            lFake.getInt( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenIntSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      Assert.assertEquals( 100, lFake.getInt( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test( expected = NullPointerException.class )
   public void whenNoIntParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      // If parameter is not set then cannot return null as primitive int, so throw NPE.
      lFake.getInt( "non-existent param name" );
   }


   // TEST getInteger()

   @Test
   public void whenIntegerNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( new Integer( LOGIC_INTEGER_PARM_DEFAULT_VALUE ),
            lFake.getInteger( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenIntegerSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      Assert.assertEquals( new Integer( 100 ), lFake.getInteger( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenNoIntegerParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertNull( lFake.getInteger( "non-existent param name" ) );
   }


   // TEST getBigDecimal()

   @Test
   public void whenBigDecimalNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( new BigDecimal( LOGIC_INTEGER_PARM_DEFAULT_VALUE ),
            lFake.getBigDecimal( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenBigDecimalSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      // Note there is no way to set a BigDecimal, can only get a BigDecimal.
      lFake.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      Assert.assertEquals( new BigDecimal( 100 ), lFake.getBigDecimal( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenNoBigDecimalParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertNull( lFake.getBigDecimal( "non-existent param name" ) );
   }


   // TEST getFloat()

   @Test
   public void whenFloatNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( new Float( LOGIC_INTEGER_PARM_DEFAULT_VALUE ),
            lFake.getFloat( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenFloatSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      // Note there is no way to set a Float, can only get a Float.
      lFake.setInteger( LOGIC_INTEGER_PARM_NAME, 100 );
      Assert.assertEquals( new Float( 100 ), lFake.getFloat( LOGIC_INTEGER_PARM_NAME ) );
   }


   @Test
   public void whenNoFloatParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertNull( lFake.getFloat( "non-existent param name" ) );
   }


   // TEST getProperty()

   @Test
   public void whenPropertyNotSet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertEquals( LOGIC_STRING_PARM_DEFAULT_VALUE,
            lFake.getProperty( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenPropertySet() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.setProperty( LOGIC_STRING_PARM_NAME, "WARN" );
      Assert.assertEquals( "WARN", lFake.getProperty( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenNoPropertyParmName() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      Assert.assertNull( lFake.getProperty( "non-existent param name" ) );
   }


   // TEST storeParameter()

   @Test
   public void whenStoreExistingParameter() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.storeParameter( LOGIC_STRING_PARM_NAME, "WARN" );
      Assert.assertEquals( "WARN", lFake.getProperty( LOGIC_STRING_PARM_NAME ) );
   }


   @Test
   public void whenStoreNewParameter() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.storeParameter( "hello", "world" );
      // New user parameters are not supported, the parameter must exist within utl_config_parm in
      // order to be overwritten in utl_user_parm.
      Assert.assertNull( lFake.getString( "hello" ) );
   }


   @Test( expected = NullPointerException.class )
   public void whenStoreNullParameter() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      // Expect an NPE if attempt to store a parameter with a NULL name.
      lFake.storeParameter( null, "" );
   }


   // TEST propertyNames(), getProperties(), exists(), remove() which are all not supported.

   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingPropertyNames() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.propertyNames();
   }


   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingGetProperties() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.getProperties();
   }


   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingExists() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.exists( LOGIC_STRING_PARM_NAME, PARM_TYPE );
   }


   @Test( expected = UnsupportedOperationException.class )
   public void whenCallingRemove() {
      UserParametersFake lFake = new UserParametersFake( USER_ID, PARM_TYPE );
      lFake.remove( LOGIC_STRING_PARM_NAME );
   }

}
