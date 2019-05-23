package com.mxi.mx.core.services.location.dataservices.validator;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.services.dataservices.transferobject.BulkLoadElementTO;
import com.mxi.mx.core.services.location.InvalidLocationCodeException;
import com.mxi.mx.core.services.location.dataservices.ChangeOfBinParentLocationException;
import com.mxi.mx.core.services.location.dataservices.InvalidBinRouteOrderException;
import com.mxi.mx.core.services.location.dataservices.Validator.BinRouteOrderValidator;


/**
 * Tests the {@link BinRouteOrderValidator} class. Used to validate the order in which exceptions
 * are thrown.
 *
 * @author srchlk
 *
 */
@RunWith( Parameterized.class )
public class BinRouteOrderValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public ExpectedException iExpectedException = ExpectedException.none();

   BinRouteOrderValidator iBinRouteOrderValidator = new BinRouteOrderValidator();

   // test parameters
   private String iC0_ParentLocation;
   private String iC1_BinLocation;
   private String iC2_RouteOrder;
   private Class<Throwable> iException;

   private static final String PARENT_LOCATION_1 = "SRVSTORE1";
   private static final String PARENT_LOCATION_2 = "SRVSTORE2";
   private static final String BIN_LOCATION = "BIN1";
   private static final String ROUTE_ORDER = "10";


   /**
    *
    * Creates a new {@linkplain BinRouteOrderValidatorTest} object. C0..C2 are components of an
    * uploaded BinRouteOrder csv file formatted as a BulkLoadElementTO transfer object
    *
    * @param aC0
    *           maps to the Parent Location Code
    * @param aC1
    *           maps to the Bin Location
    * @param aC2
    *           maps to the Route Order
    * @param aException
    *           the type of exception the validator is expected to throw. Null if no exception is
    *           expected
    */
   public BinRouteOrderValidatorTest(String aC0, String aC1, String aC2,
         Class<Throwable> aException) {
      iC0_ParentLocation = aC0;
      iC1_BinLocation = aC1;
      iC2_RouteOrder = aC2;
      iException = aException;
   }


   @Before
   public void setup() {
      // create a srvstore type parent location
      LocationKey lParentLocation_1 = Domain.createLocation( location -> {
         location.setCode( PARENT_LOCATION_1 );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( true );
      } );

      // create a srvstore type parent location
      Domain.createLocation( location -> {
         location.setCode( PARENT_LOCATION_2 );
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setIsSupplyLocation( true );
      } );

      // create a bin location
      Domain.createLocation( location -> {
         location.setCode( BIN_LOCATION );
         location.setType( RefLocTypeKey.BIN );
         location.setIsSupplyLocation( true );
         location.setParent( lParentLocation_1 );

      } );

   }


   @Parameterized.Parameters
   public static Collection<Object[]> ErrorScenarios() {
      return Arrays.asList( new Object[][] {
            // MandatoryArgumentException when the parent location code is null
            { "", BIN_LOCATION, ROUTE_ORDER, MandatoryArgumentException.class },

            // MandatoryArgumentException when the bin location code does is null
            { PARENT_LOCATION_1, "", ROUTE_ORDER, MandatoryArgumentException.class },

            // InvalidLocationCodeException when the parent location code does not exist
            { "WrongParentLocationCd", BIN_LOCATION, ROUTE_ORDER,
                  InvalidLocationCodeException.class },

            // InvalidLocationCodeException when the bin location code does not exist
            { PARENT_LOCATION_1, "WrongBinLocationCd", ROUTE_ORDER,
                  InvalidLocationCodeException.class },

            // InvalidBinRouteOrderException when the route order is a string, symbol or decimal
            { PARENT_LOCATION_1, BIN_LOCATION, "ABC", InvalidBinRouteOrderException.class },

            // NegativeValueException when the route order is a negative value
            { PARENT_LOCATION_1, BIN_LOCATION, "-9", NegativeValueException.class },

            // ChangeOfBinParentLocationException when the parent locations of a bin location is
            // changed through csv.
            { PARENT_LOCATION_2, BIN_LOCATION, ROUTE_ORDER,
                  ChangeOfBinParentLocationException.class },

            // No exception was thrown when data is valid
            { PARENT_LOCATION_1, BIN_LOCATION, ROUTE_ORDER, null }

      } );

   }


   @Test
   public void test_validateBeforeInsert() throws Exception {

      // if an exception is expected, set the ExpectedException jUnit rule.
      if ( iException != null ) {
         iExpectedException.expect( iException );
      }

      // create a BulkLoadElementTo transfer object and pass it to the validateBeforeInsert method
      // validateBeforInsert is called instead of validateCommon because it contains all validations
      iBinRouteOrderValidator.validateBeforeInsert( new BulkLoadElementTO( 0,
            RefBulkLoadFileActionKey.BIN_ROUTE_ORDER, RefBulkLoadStatusKey.PROCESSING, "",
            iC0_ParentLocation, iC1_BinLocation, iC2_RouteOrder ) );
   }

}
