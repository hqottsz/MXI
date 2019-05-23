package com.mxi.mx.core.services.location.dataservices;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 * Tests the ChangeOfBinParentLocationException class
 *
 * @author srchlk
 *
 */
public class ChangeOfBinParentLocationExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String PARENT_LOCATION_1 = "SRVSTORE1";
   private static final String PARENT_LOCATION_2 = "SRVSTORE2";
   private static final String BIN_LOCATION_1 = "BIN1";


   /**
    *
    * GIVEN a bin location, WHEN parent location of a bin is not changed through csv, THEN
    * ChangeOfBinParentLocationException should not be raised
    *
    * @throws ChangeOfBinParentLocationException
    */

   @Test( expected = Test.None.class )
   public void testCorrectParentLocationAndBinLocation() throws ChangeOfBinParentLocationException {

      ChangeOfBinParentLocationException.validate( BIN_LOCATION_1, PARENT_LOCATION_1 );

   }


   /**
    *
    * GIVEN a bin location, WHEN parent location of a bin is changed through csv, THEN
    * ChangeOfBinParentLocationException should be raised
    *
    * @throws ChangeOfBinParentLocationException
    */
   @Test( expected = ChangeOfBinParentLocationException.class )
   public void testChangeParentLocationOfBin() throws ChangeOfBinParentLocationException {

      ChangeOfBinParentLocationException.validate( BIN_LOCATION_1, PARENT_LOCATION_2 );

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
         location.setCode( BIN_LOCATION_1 );
         location.setType( RefLocTypeKey.BIN );
         location.setIsSupplyLocation( true );
         location.setParent( lParentLocation_1 );

      } );

   }

}
