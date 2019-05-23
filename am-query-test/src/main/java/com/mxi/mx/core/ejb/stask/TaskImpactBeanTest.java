package com.mxi.mx.core.ejb.stask;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Impact;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.taskimpact.TaskImpactBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.EventService;
import com.mxi.mx.core.table.org.OrgHr;


/**
 *
 * Verifies the behaviour of {@link TaskImpactBean}
 *
 */
public class TaskImpactBeanTest {

   private static final String IMPACT_DESCRIPTION = "Impact Description";
   private static final String UPDATED_IMPACT_DESCRIPTION = "Updated Impact Description";

   private static final String IMPACT_CODE = "TEST_IMPACT_CD";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iAuthorizingHr;
   private int iUserId;


   /**
    * <pre>
    * Given that I am a Maintenance Engineer and there is an adhoc task
    * When an impact is added on the adhoc task
    * Then the task will have the added impact and its description
    * </pre>
    */
   @Test
   public void itAddsImpactToAdhocTask() throws Exception {

      final InventoryKey lAircraft = Domain.createAircraft();

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aTask ) {
                  aTask.setInventory( lAircraft );
                  aTask.setTaskClass( RefTaskClassKey.ADHOC );
                  aTask.setStatus( ACTV );
               }

            } );

      RefImpactKey lImpactKey = Domain.createImpact( new DomainConfiguration<Impact>() {

         @Override
         public void configure( Impact aImpact ) {
            aImpact.setImpactCode( IMPACT_CODE );
         }

      } );

      HashMap<RefImpactKey, String> lImpactsMap = new HashMap<RefImpactKey, String>();
      lImpactsMap.put( lImpactKey, IMPACT_DESCRIPTION );

      new TaskImpactBean().add( lRequirement, lImpactsMap );

      Map<RefImpactKey, String> lImpacts = null;
      lImpacts = EventService.getImpacts( lRequirement );

      Assert.assertEquals( "Expected impact added to task.", 1, lImpacts.size() );

      Assert.assertEquals( "Expected created impact added to task.", true,
            lImpacts.containsKey( lImpactKey ) );

      Assert.assertEquals( "Expected impact description added to task.", IMPACT_DESCRIPTION,
            lImpacts.get( lImpactKey ) );

   }


   /**
    * <pre>
    * Given that I am a Maintenance Engineer and there is an adhoc task with an impact
    * When an impact description is modified
    * Then the task will have the modified impact description
    * </pre>
    */
   @Test
   public void itUpdatesAdhocTaskImpact() throws Exception {

      final RefImpactKey lImpactKey = Domain.createImpact( new DomainConfiguration<Impact>() {

         @Override
         public void configure( Impact aImpact ) {
            aImpact.setImpactCode( IMPACT_CODE );
         }

      } );

      final InventoryKey lAircraft = Domain.createAircraft();

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aTask ) {
                  aTask.setInventory( lAircraft );
                  aTask.setTaskClass( RefTaskClassKey.ADHOC );
                  aTask.setStatus( ACTV );
                  aTask.addImpact( lImpactKey, IMPACT_DESCRIPTION );
               }

            } );

      HashMap<RefImpactKey, String> lImpactsMap = new HashMap<RefImpactKey, String>();
      lImpactsMap.put( lImpactKey, UPDATED_IMPACT_DESCRIPTION );

      new TaskImpactBean().update( lRequirement, lImpactsMap );

      Map<RefImpactKey, String> lImpacts = null;
      lImpacts = EventService.getImpacts( lRequirement );

      Assert.assertEquals( "Expected task has impact.", 1, lImpacts.size() );

      Assert.assertEquals( "Expected the impact still linked to the task.", true,
            lImpacts.containsKey( lImpactKey ) );

      Assert.assertEquals( "Expected impact description updated.", UPDATED_IMPACT_DESCRIPTION,
            lImpacts.get( lImpactKey ) );

   }


   /**
    * <pre>
    * Given that I am a Maintenance Engineer and there is an adhoc task with an impact
    * When an impact is removed from the task
    * Then the task will not have the impact
    * </pre>
    */
   @Test
   public void itRemovesAdhocTaskImpact() throws Exception {

      final RefImpactKey lImpactKey = Domain.createImpact();

      final InventoryKey lAircraft = Domain.createAircraft();

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aTask ) {
                  aTask.setInventory( lAircraft );
                  aTask.setTaskClass( RefTaskClassKey.ADHOC );
                  aTask.setStatus( ACTV );
                  aTask.addImpact( lImpactKey, IMPACT_DESCRIPTION );
               }

            } );

      List<RefImpactKey> lImpactKeys = new ArrayList<RefImpactKey>();
      lImpactKeys.add( lImpactKey );

      new TaskImpactBean().delete( lRequirement, lImpactKeys );

      Map<RefImpactKey, String> lImpacts = null;
      lImpacts = EventService.getImpacts( lRequirement );

      Assert.assertEquals( "Expected task has impact.", 0, lImpacts.size() );

   }


   @Before
   public void setup() {

      iAuthorizingHr = new HumanResourceDomainBuilder().build();
      iUserId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();

      UserParametersFake lUserParametersFake = new UserParametersFake( iUserId, "LOGIC" );
      lUserParametersFake.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParametersFake );
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }

}
