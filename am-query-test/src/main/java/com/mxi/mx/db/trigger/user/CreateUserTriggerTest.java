package com.mxi.mx.db.trigger.user;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.license.exception.LicenseException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.ejb.user.UserBean;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.services.user.UserDetailsTO;


/**
 * This class tests the User modified trigger for creating a user.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreateUserTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;
   private static final String FIRSTNAME = "MXI1";
   private static final String LASTNAME = "mxi";
   private static final String USERNAME = "USER1";
   private static final String HR_CODE = "TESTHR";
   private static final OrgKey orgKey = new OrgKey( 31, 4650 );

   private UserBean userBean = new UserBean();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() throws MxException {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      triggerCalled = false;
      TriggerFactory.setInstance( null );
   }


   @Test
   public void testCreateUserTrigger() throws LicenseException, MxException, TriggerException {
      UserDetailsTO userDetailsTO = new UserDetailsTO();
      userDetailsTO.setFirstName( FIRSTNAME );
      userDetailsTO.setLastName( LASTNAME );
      userDetailsTO.setUsername( USERNAME );
      userDetailsTO.setHrCode( HR_CODE );
      userDetailsTO.setOrganizationKey( orgKey );
      userBean.create( userDetailsTO );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked
    */
   @Override
   public void after( UUID userId ) throws TriggerException {
      triggerCalled = true;
   }

}
