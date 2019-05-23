package com.mxi.am.api.resource.maintenance.prog.taskdefn.weightbalance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.prog.taskdefn.weightbalance.impl.WeightBalanceResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.materials.tracking.measurement.datatype.dao.DataTypeDao;
import com.mxi.mx.core.materials.tracking.measurement.datatype.dao.JdbcDataTypeEntityDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test for the Weight Balance REST API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class WeightBalanceResourceBeanTest extends ResourceBeanTest {

   WeightBalance weightBalance1 = new WeightBalance();
   WeightBalance weightBalance2 = new WeightBalance();
   WeightBalance weightBalance3 = new WeightBalance();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( Security.class ).to( CoreSecurity.class );
               bind( DataTypeDao.class ).to( JdbcDataTypeEntityDao.class );
            }
         } );

   @Inject
   WeightBalanceResourceBean testWeightBalanceResource;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void init() throws MxException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      testWeightBalanceResource.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
      constructWeightBalances();
   }


   /**
    * Simple get by id, returns a single result.
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void get_success() throws AmApiResourceNotFoundException {
      WeightBalance result = testWeightBalanceResource.get( weightBalance1.getId() );

      assertWeightBalance( weightBalance1, result );
   }


   /**
    * Test a get-by-id get with no results - throws an exception.
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_noResults() throws AmApiResourceNotFoundException {
      testWeightBalanceResource.get( "99999999999999999999999999999999" );
   }


   /**
    * Find multiple results related to a single task definition.
    *
    */
   @Test
   @CSIContractTest( Project.SWA_WP_STATUS )
   public void search_success_byTaskDefMultipleResults() {
      WeightBalanceSearchParameters parameters = new WeightBalanceSearchParameters();
      parameters.setTaskDefinitionId( "00000000000000000000000000000102" );

      List<WeightBalance> results = testWeightBalanceResource.search( parameters );
      assertEquals( "Unexpected number of results found: ", 2, results.size() );

      if ( results.get( 0 ).getId().equals( weightBalance2.getId() ) ) {
         assertWeightBalance( weightBalance2, results.get( 0 ) );
         assertWeightBalance( weightBalance3, results.get( 1 ) );
      } else {
         assertWeightBalance( weightBalance2, results.get( 1 ) );
         assertWeightBalance( weightBalance3, results.get( 0 ) );
      }
   }


   /**
    * Test a search by a parameter that isn't in the db, get no results.
    */
   @Test
   public void search_success_noResults() {
      WeightBalanceSearchParameters parameters = new WeightBalanceSearchParameters();
      parameters.setTaskDefinitionId( "99999999999999999999999999999999" );

      List<WeightBalance> results = testWeightBalanceResource.search( parameters );
      assertEquals( "Unexpected number of results found: ", 0, results.size() );

   }


   private void assertWeightBalance( WeightBalance expected, WeightBalance actual ) {
      if ( actual == null ) {
         fail( "Returned weight balance was null." );
      }
      assertEquals( "Incorrect id returned: ", expected.getId(), actual.getId() );
      assertEquals( "Incorrect part id returned: ", expected.getPartId(), actual.getPartId() );
      assertEquals( "Incorrect weight returned: ", expected.getWeight(), actual.getWeight(), 0.0 );
      assertEquals( "Incorrect moment returned: ", expected.getMoment(), actual.getMoment(), 0.0 );
      assertEquals( "Incorrect task definition id returned: ", expected.getTaskDefinitionId(),
            actual.getTaskDefinitionId() );

   }


   private void constructWeightBalances() {
      weightBalance1.setId( "00000000000000000000000000000001" );
      weightBalance1.setPartId( null );
      weightBalance1.setWeight( 1.1 );
      weightBalance1.setMoment( 2.2 );
      weightBalance1.setTaskDefinitionId( "00000000000000000000000000000101" );

      weightBalance2.setId( "00000000000000000000000000000002" );
      weightBalance2.setPartId( null );
      weightBalance2.setWeight( 3.3 );
      weightBalance2.setMoment( 4.4 );
      weightBalance2.setTaskDefinitionId( "00000000000000000000000000000102" );

      weightBalance3.setId( "00000000000000000000000000000003" );
      weightBalance3.setPartId( "00000000000000000000000000001001" );
      weightBalance3.setWeight( 5.5 );
      weightBalance3.setMoment( 6.6 );
      weightBalance3.setTaskDefinitionId( "00000000000000000000000000000102" );
   }

}
