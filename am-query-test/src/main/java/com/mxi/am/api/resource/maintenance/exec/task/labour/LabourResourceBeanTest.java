package com.mxi.am.api.resource.maintenance.exec.task.labour;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJBContext;

import org.junit.Assert;
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
import com.mxi.am.api.exception.AmApiAuthorizationException;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.exec.task.labour.impl.LabourResourceBean;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Tests the labour API functionality
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class LabourResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( LabourResource.class ).to( LabourResourceBean.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( SchedStaskDao.class ).to( JdbcSchedStaskDao.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );

            }
         } );

   @Inject
   LabourResourceBean iLabourResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   private static final String LABOUR_ID_1 = "9036AE8A6F8411E6A9A99167A0CBBFF4";
   private static final String LABOUR_ID_2 = "9036AE8A6F8411E6A9A99167A0CABCD4";
   private static final String LABOUR_ID_3 = "9036AE8A6F8411E6A9A99167A0CABCD5";
   private static final String ACTV_STAGE_CD = "ACTV";
   private static final String IN_WORK_STAGE_CD = "IN WORK";
   private static final String AFT_SKILL = "AFT";
   private static final String TASK_ID_1 = "7636CF8A6F7362E6A9A84762A0CCAFE2";
   private static final String TASK_ID_2 = "7636CF8A6F7362E6A9A84762A0CCAFE4";
   private static final String TASK_ID_3 = "7636CF8A6F7362E6A9A84762A0CCAFE3";

   private static final String INVALID_ID = "9036AE8A6F8411E6A9A99167A0CAAAA1";

   private static final String RMVD_INV_ID = "A3A7522844D311E8AA84309C2333CAEC";
   private static final String RMVD_PART_REQUIREMENT_ID = "C059836144D311E89AE62FB8DAC8CAD7";
   private static final String RMVD_REASON = "IMSCHD";
   private static final double RMVD_QUANTITY = 1;

   private static final String INST_INV_ID = "D1805449440A11E88BC3309C2333CAEC";
   private static final String INST_PART_REQUIREMENT_ID = "B7CF3BDB44D311E89AE62FB8DAC8CAD7";
   private static final double INST_QUANTITY = 1;

   private static final double ACTUAL_HOURS = 0.0;
   private static final double SCHEDULED_HOURS = 1.0;
   private static final double ADJUSTED_BILLING_HOURS = 2.0;

   private static final String TOOL_PART_ID = "C7CF3BDB44D311E89AE62FB8DAC8CAD7";
   private static final String TOOL_INV_ID = "D7CF3BDB44D311E89AE62FB8DAC8CAD7";
   private static final String TOOL_PART_NUMBER = "TOOL";
   private static final String TOOL_MANUFACTURER_CODE = "manufact";
   private static final String TOOL_INV_SERIAL_NUMBER = "tool_inv";

   Labour iLabour1 = new Labour();
   Labour iLabour2 = new Labour();
   Labour iLabour3 = new Labour();


   @Before
   public void setUp() throws MxException {
      InjectorContainer.get().injectMembers( this );
      iLabourResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();
      constructLabourObjects();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   @CSIContractTest( Project.UPS )
   public void getLabourSuccess() throws AmApiResourceNotFoundException {
      Labour actualLabour = iLabourResourceBean.get( iLabour1.getId() );
      assertLaboursEqual( iLabour1, actualLabour );
   }


   @Test
   public void getNoLabourFound() {
      Labour returnedLabour = null;
      String responseMessage = null;
      try {
         returnedLabour = iLabourResourceBean.get( INVALID_ID );
      } catch ( AmApiResourceNotFoundException e ) {
         responseMessage = e.getMessage();
      }
      assertEquals( responseMessage, "LABOUR " + INVALID_ID + " not found" );
      assertNull( returnedLabour );
   }


   @Test
   public void updateLabourSuccess() throws AmApiResourceNotFoundException, AmApiBusinessException {
      Labour lLabour = iLabourResourceBean.get( iLabour1.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );

      Labour returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      assertLaboursEqual( lLabour, returnedLabour );
   }


   @Test
   public void updateLabourWorkpackageNotInWorkFailure() throws AmApiResourceNotFoundException {
      Labour lLabour = iLabourResourceBean.get( iLabour3.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      String responseMessage = null;
      Labour returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertTrue( responseMessage.contains( "[MXERR-30329]" ) );
   }


   @Test
   public void updateLabourWorkpackageClassNotCheckFailure() throws AmApiResourceNotFoundException {
      Labour lLabour = iLabourResourceBean.get( iLabour3.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      String responseMessage = null;
      Labour returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertTrue( responseMessage.contains( "[MXERR-30329]" ) );
   }


   @Test
   public void updateLabourTaskNotAssignedToWPFailure() throws AmApiResourceNotFoundException {
      Labour lLabour = iLabourResourceBean.get( iLabour2.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      String responseMessage = null;
      Labour returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertTrue( responseMessage.contains( "[MXERR-32307]" ) );
   }


   @Test
   public void updateLabourNoTechRoleFailure()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      Labour lLabour = iLabourResourceBean.get( iLabour1.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      lLabour.setLabourRoles( null );
      String responseMessage = null;
      Labour returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertEquals( responseMessage, "A TECH role must be specified." );
   }


   @Test
   public void updateLabourNoTechRoleUserFailure()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      Labour lLabour = iLabourResourceBean.get( iLabour1.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      lLabour.setLabourRoles( null );
      LabourRole lLabourRole =
            getLabourRole( RefLabourRoleTypeKey.TECH.getCd(), RefLabourRoleStatusKey.ACTV.getCd() );
      lLabourRole.setUsername( null );
      lLabour.setLabourRoles( Collections.singleton( lLabourRole ) );
      String responseMessage = null;
      Labour returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertEquals( responseMessage, "Invalid tech username provided" );
   }


   @Test
   public void updateLabourTechRoleUserNotFoundFailure()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      Labour lLabour = iLabourResourceBean.get( iLabour1.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      lLabour.setLabourRoles( null );
      LabourRole lLabourRole =
            getLabourRole( RefLabourRoleTypeKey.TECH.getCd(), RefLabourRoleStatusKey.ACTV.getCd() );
      lLabourRole.setUsername( "notfound" );
      lLabour.setLabourRoles( Collections.singleton( lLabourRole ) );
      String responseMessage = null;
      Labour returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertEquals( responseMessage, "Invalid tech username provided" );
   }


   @Test
   public void updateLabourAlreadyStartedByAnotherUserFailure()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      // start the labour
      Labour lLabour = iLabourResourceBean.get( iLabour1.getId() );
      assertEquals( ACTV_STAGE_CD, lLabour.getLabourStageCode() );
      lLabour.setLabourStageCode( IN_WORK_STAGE_CD );
      Labour returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      assertLaboursEqual( lLabour, returnedLabour );

      // try to start labour again with difference user
      lLabour.setLabourRoles( null );
      LabourRole lLabourRole =
            getLabourRole( RefLabourRoleTypeKey.TECH.getCd(), RefLabourRoleStatusKey.ACTV.getCd() );
      lLabourRole.setUsername( "ADMIN" );
      lLabour.setLabourRoles( Collections.singleton( lLabourRole ) );
      String responseMessage = null;
      returnedLabour = null;
      try {
         returnedLabour = iLabourResourceBean.update( lLabour.getId(), lLabour );
      } catch ( AmApiBusinessException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertTrue( responseMessage.contains( "Labour already started by a different tech user" ) );
   }


   @Test
   public void updateLabourNoPayloadFailure()
         throws AmApiResourceNotFoundException, AmApiBusinessException {

      Labour lLabour = null;
      Labour returnedLabour = null;
      String responseMessage = null;
      try {
         returnedLabour = iLabourResourceBean.update( iLabour1.getId(), lLabour );
      } catch ( AmApiBadRequestException e ) {
         responseMessage = e.getMessage();
      }
      assertNull( returnedLabour );
      assertEquals( responseMessage, "Missing labour payload" );
   }


   @Test
   public void getUnauthorizedPrinciple() throws AmApiResourceNotFoundException {
      Mockito.when( iPrincipal.getName() ).thenReturn( UNAUTHORIZED );
      try {
         iLabourResourceBean.get( LABOUR_ID_1 );
         Assert.fail( "Expected exception" );
      } catch ( AmApiAuthorizationException aE ) {
      }
   }


   @Test
   public void updateUnauthorizedPrinciple()
         throws AmApiResourceNotFoundException, AmApiBusinessException {
      Mockito.when( iPrincipal.getName() ).thenReturn( UNAUTHORIZED );
      try {
         iLabourResourceBean.update( LABOUR_ID_1, iLabour1 );
         Assert.fail( "Expected exception" );
      } catch ( AmApiAuthorizationException aE ) {
      }
   }


   private void constructLabourObjects() {

      iLabour1.setId( LABOUR_ID_1 );
      iLabour1.setLabourStageCode( ACTV_STAGE_CD );
      iLabour1.setSkill( AFT_SKILL );
      iLabour1.setTaskId( TASK_ID_1 );
      iLabour1.addInstalledInventory( getInstalledInventory() );
      iLabour1.addRemovedInventory( getRemovedInventory() );
      iLabour1.addLabourRole( getLabourRole( RefLabourRoleTypeKey.TECH.getCd(),
            RefLabourRoleStatusKey.ACTV.getCd() ) );
      iLabour1.addLabourRole( getLabourRole( RefLabourRoleTypeKey.CERT.getCd(),
            RefLabourRoleStatusKey.PENDING.getCd() ) );
      iLabour1.addLabourRole( getLabourRole( RefLabourRoleTypeKey.INSP.getCd(),
            RefLabourRoleStatusKey.PENDING.getCd() ) );
      iLabour1.setTools( getTools() );

      iLabour2.setId( LABOUR_ID_2 );
      iLabour2.setLabourStageCode( ACTV_STAGE_CD );
      iLabour2.setSkill( AFT_SKILL );
      iLabour2.setTaskId( TASK_ID_2 );
      iLabour2.addLabourRole( getLabourRole( RefLabourRoleTypeKey.TECH.getCd(),
            RefLabourRoleStatusKey.ACTV.getCd() ) );

      iLabour3.setId( LABOUR_ID_3 );
      iLabour3.setLabourStageCode( ACTV_STAGE_CD );
      iLabour3.setSkill( AFT_SKILL );
      iLabour3.setTaskId( TASK_ID_3 );
      iLabour3.addLabourRole( getLabourRole( RefLabourRoleTypeKey.TECH.getCd(),
            RefLabourRoleStatusKey.ACTV.getCd() ) );
   }


   private LabourRole getLabourRole( String aRoleCode, String aStatusCode ) {
      LabourRole lLabourRole = new LabourRole();
      lLabourRole.setRole( aRoleCode );
      lLabourRole.setUsername( AUTHORIZED );
      lLabourRole.setActualHours( ACTUAL_HOURS );
      lLabourRole.setScheduledHours( SCHEDULED_HOURS );
      lLabourRole.setAdjustedBillingHours( ADJUSTED_BILLING_HOURS );
      lLabourRole.setStatusCode( aStatusCode );
      return lLabourRole;
   }


   private Set<Tool> getTools() {
      Tool lTool = new Tool();
      lTool.setPartId( TOOL_PART_ID );
      lTool.setInventoryId( TOOL_INV_ID );
      lTool.setPartNumber( TOOL_PART_NUMBER );
      lTool.setManufacturerCode( TOOL_MANUFACTURER_CODE );
      lTool.setInventorySerialNumber( TOOL_INV_SERIAL_NUMBER );
      Set<Tool> lTools = new HashSet<>( 1 );
      lTools.add( lTool );
      return lTools;
   }


   private InstalledInventory getInstalledInventory() {
      InstalledInventory lInstalledInventory = new InstalledInventory();
      lInstalledInventory.setInventoryId( INST_INV_ID );
      lInstalledInventory.setInstalledQuantity( INST_QUANTITY );
      lInstalledInventory.setPartRequirementId( INST_PART_REQUIREMENT_ID );

      return lInstalledInventory;
   }


   private RemovedInventory getRemovedInventory() {
      RemovedInventory lRemovedInventory = new RemovedInventory();
      lRemovedInventory.setInventoryId( RMVD_INV_ID );
      lRemovedInventory.setPartRequirementId( RMVD_PART_REQUIREMENT_ID );
      lRemovedInventory.setRemovalReasonCode( RMVD_REASON );
      lRemovedInventory.setRemovedQuantity( RMVD_QUANTITY );

      return lRemovedInventory;
   }


   private void assertLaboursEqual( Labour expectedLabour, Labour actualLabour ) {
      assertEquals( "Incorrect labour ID found in retrieved labour.", expectedLabour.getId(),
            actualLabour.getId() );
      assertThat( "Incorrect labour roles found in retrieved labour.",
            expectedLabour.getLabourRoles(),
            containsInAnyOrder( actualLabour.getLabourRoles().toArray() ) );
      assertEquals( "Incorrect labour stage code found in retrieved labour.",
            expectedLabour.getLabourStageCode(), actualLabour.getLabourStageCode() );
      assertEquals( "Incorrect labour skill found in retrieved labour.", expectedLabour.getSkill(),
            actualLabour.getSkill() );
      assertEquals( "Incorrect task ID found in retrieved labour.", expectedLabour.getTaskId(),
            actualLabour.getTaskId() );

      // verify tool reqs
      assertThat( "Incorrect labour tools found in retrieved labour.", expectedLabour.getTools(),
            containsInAnyOrder( actualLabour.getTools().toArray() ) );

      // Verify installed inventory details
      assertEquals( "Incorrect installed inventory ID found in retrieved labour.",
            expectedLabour.getInstalledInventories().get( 0 ).getInventoryId(),
            actualLabour.getInstalledInventories().get( 0 ).getInventoryId() );
      assertEquals( "Incorrect installed inventory installed quantity found in retrieved labour.",
            expectedLabour.getInstalledInventories().get( 0 ).getInstalledQuantity(),
            actualLabour.getInstalledInventories().get( 0 ).getInstalledQuantity(), 0 );
      assertEquals( "Incorrect installed inventory part requirement ID found in retrieved labour.",
            expectedLabour.getInstalledInventories().get( 0 ).getPartRequirementId(),
            actualLabour.getInstalledInventories().get( 0 ).getPartRequirementId() );

      // Verify removed inventory details
      assertEquals( "Incorrect removed inventory ID found in retrieved labour.",
            expectedLabour.getRemovedInventories().get( 0 ).getInventoryId(),
            actualLabour.getRemovedInventories().get( 0 ).getInventoryId() );
      assertEquals( "Incorrect removed inventory part requirement ID found in retrieved labour.",
            expectedLabour.getRemovedInventories().get( 0 ).getPartRequirementId(),
            actualLabour.getRemovedInventories().get( 0 ).getPartRequirementId() );
      assertEquals( "Incorrect removed inventory removal reason code found in retrieved labour.",
            expectedLabour.getRemovedInventories().get( 0 ).getRemovalReasonCode(),
            actualLabour.getRemovedInventories().get( 0 ).getRemovalReasonCode() );
      assertEquals( "Incorrect removed inventory removed quantity found in retrieved labour.",
            expectedLabour.getRemovedInventories().get( 0 ).getRemovedQuantity(),
            actualLabour.getRemovedInventories().get( 0 ).getRemovedQuantity(), 0 );
   }

}
