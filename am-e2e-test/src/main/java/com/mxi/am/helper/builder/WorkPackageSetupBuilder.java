package com.mxi.am.helper.builder;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Row;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.helper.TimestampGenerator;
import com.mxi.driver.api.ApiDriver;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.wpl.api.induction.action.create.v1.WorkPackageSetupCreateParameter;
import com.mxi.mx.wpl.api.induction.action.create.v1.WorkPackageSetupCreateRequest;
import com.mxi.mx.wpl.api.induction.action.create.v1.WorkPackageSetupCreateResponse;


/**
 * Creates a work package setup using the Create WorkPackageSetup API.
 * <ul>
 * <li>Name is defaulted to "WP_<timestamp>"</li>
 * <li>Scheduled start date is defaulted to the current date.</li>
 * <li>Scheduled end date is defaulted to the current date plus 30 days.</li>
 * </ul>
 *
 */
public class WorkPackageSetupBuilder {

   private static final String DEFAULT_ACCOUNT_CODE = "5";

   private static final Date TODAY = Calendar.getInstance().getTime();
   private ApiDriver iApiDriver;
   private String iName = "WP_" + TimestampGenerator.generate();
   private String iAircraftRegCode = "100";
   private String iLocation = "A1";
   private String iAccountId;
   private Date iSchedStartDate = TODAY;
   private Date iSchedEndDate = DateUtils.addDays( TODAY, 30 );

   private DatabaseDriver iDatabaseDriver;


   @Inject
   public WorkPackageSetupBuilder(@AssetManagement URI aRsUri,
         @AssetManagement DatabaseDriver aDbDriver) {
      iDatabaseDriver = aDbDriver;
      iApiDriver = new ApiDriver( aRsUri.resolve( "maintenix" ) );
      iApiDriver.login( "mxi", "password" );
   }


   /**
    *
    * Creates the work package setup using the Create Work Package API (WorkPackageSetupCreate api)
    *
    * @return id of the work package setup
    */
   public String build() {

      validateMandatoryArguments();

      if ( iAccountId == null ) {
         iAccountId = getDefaultAccount();
      }

      WorkPackageSetupCreateParameter lWorkPackageSetupParameter =
            new WorkPackageSetupCreateParameter();
      lWorkPackageSetupParameter.setName( iName );
      lWorkPackageSetupParameter.setRegistrationCode( iAircraftRegCode );
      lWorkPackageSetupParameter.setAccountId( iAccountId );
      lWorkPackageSetupParameter.setLocation( iLocation );
      lWorkPackageSetupParameter.setStartDate( iSchedStartDate );
      lWorkPackageSetupParameter.setEndDate( iSchedEndDate );

      // create the request
      WorkPackageSetupCreateRequest lWorkPackageSetupRequest = new WorkPackageSetupCreateRequest();
      lWorkPackageSetupRequest.setActionParameters( Arrays.asList( lWorkPackageSetupParameter ) );

      // send the request
      WorkPackageSetupCreateResponse lCreateWorkPackageResponse =
            iApiDriver.execute( lWorkPackageSetupRequest );

      return lCreateWorkPackageResponse.getValidActions().iterator().next().getWorkPackageSetup();
   }


   public WorkPackageSetupBuilder withName( String aName ) {
      iName = aName;
      return this;
   }


   public WorkPackageSetupBuilder withAircraftRegCode( String aAircraftRegCode ) {
      iAircraftRegCode = aAircraftRegCode;
      return this;
   }


   public WorkPackageSetupBuilder withLocation( String aLocation ) {
      iLocation = aLocation;
      return this;
   }


   public WorkPackageSetupBuilder withSchedStartDate( Date aSchedStartDate ) {
      iSchedStartDate = new Date( aSchedStartDate.getTime() );
      return this;
   }


   public WorkPackageSetupBuilder withSchedEndDate( Date aSchedEndDate ) {
      iSchedEndDate = new Date( aSchedEndDate.getTime() );
      return this;
   }


   public WorkPackageSetupBuilder withAccountId( String aAccountId ) {
      iAccountId = aAccountId;
      return this;
   }


   /**
    * Returns the default location
    *
    * @return
    */
   private String getDefaultAccount() {

      Row lQueryRow = iDatabaseDriver
            .select( "SELECT RAWTOHEX(alt_id) AS alt_id FROM fnc_account WHERE account_cd = ?",
                  DEFAULT_ACCOUNT_CODE )
            .get( 0 );

      return lQueryRow.get( "alt_id" );
   }


   /**
    *
    * validates that all mandatory arguments have been set
    *
    * @throws RuntimeException
    *            if mandatory argument is null
    *
    */
   private void validateMandatoryArguments() {

      if ( iAircraftRegCode == null ) {
         throw new RuntimeException( "Aircraft registration code is mandatory." );
      }

      if ( iLocation == null ) {
         throw new RuntimeException( "Location code is mandatory." );
      }

   }
}
