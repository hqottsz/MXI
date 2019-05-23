package com.mxi.am.api.resource.materials.asset.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.security.Principal;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.resource.materials.asset.inventory.impl.InventoryResourceBean;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Regressions test associated with managing the Inventory ConditionCode
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class InventoryResourceConditionTest extends ResourceBeanTest {

   @Rule
   public ExpectedException exceptionRule = ExpectedException.none();

   @Rule
   public InjectionOverrideRule injectionRule = new InjectionOverrideRule();

   private static final String PARENT_WITH_CHILD_ID = "FF99005DC21C11E893510050568B2740";
   private static final String PARENT_WITHOUT_CHILD_ID = "FF99005DC21C11E893510050568B2741";
   private static final String CHILD1_ID = "FF99005DC21C11E893510050568B2742";
   private static final String CHILD2_ID = "FF99005DC21C11E893510050568B2743";
   private static final String CHILD3_ID = "FF99005DC21C11E893510050568B2744";

   private static final String INSPREQ = RefInvCondKey.INSPREQ.getCd();
   private static final String RFI = RefInvCondKey.RFI.getCd();
   private static final String INSRV = RefInvCondKey.INSRV.getCd();

   @Inject
   InventoryResourceBean inventoryResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;


   @Before
   public void setUp() throws MxException, AmApiBusinessException {
      InjectorContainer.get().injectMembers( this );

      inventoryResourceBean.setEJBContext( iEJBContext );
      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

      // Stuff that's here just to make ResourceBean.initializeTest() stop throwing NPEs
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      // SWA uses INSTALLED_INVENTORY_NOT_APPLICABLE = "ERROR".
      // Can't figure out how to set it using GlobalParametersFake, so setting it for the authorized
      // user.
      int userId = 2;
      UserParametersFake userParms = new UserParametersFake( userId, ParmTypeEnum.LOGIC.name() );
      userParms.setString( "INSTALLED_INVENTORY_NOT_APPLICABLE", "ERROR" );
      UserParameters.setInstance( userId, ParmTypeEnum.LOGIC.name(), userParms );

   }


   @Test
   public void testDetachInventoryToConditionRFI() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD1_ID );
      childInventory.setConditionCode( RFI );
      childInventory.setParentId( "" );

      Inventory updatedChild = inventoryResourceBean.update( CHILD1_ID, childInventory, null );

      assertNull( "child is still attached", updatedChild.getParentId() );
      assertEquals( "child conditionCode is wasn't updated correctly.", RFI,
            updatedChild.getConditionCode() );
   }


   @Test
   public void testAttachRFIInventory() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD2_ID );
      childInventory.setParentId( PARENT_WITHOUT_CHILD_ID );
      childInventory.setConditionCode( INSRV );

      Inventory updatedChild = inventoryResourceBean.update( CHILD2_ID, childInventory, null );

      assertEquals( "child parent wasn't modified", PARENT_WITHOUT_CHILD_ID,
            updatedChild.getParentId() );
      assertEquals( "child conditionCode is wasn't updated", INSRV,
            updatedChild.getConditionCode() );
   }


   @Test
   public void testDetachInventoryToConditionINSPREQ() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD1_ID );
      childInventory.setConditionCode( INSPREQ );
      childInventory.setParentId( "" );

      Inventory updatedChild = inventoryResourceBean.update( CHILD1_ID, childInventory, null );

      assertNull( "child is still attached", updatedChild.getParentId() );
      assertEquals( "child conditionCode is wasn't updated correctly.", INSPREQ,
            updatedChild.getConditionCode() );
   }


   @Test
   public void testAttachRFIInventoryUnspecifiedCondition() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD2_ID );
      childInventory.setParentId( PARENT_WITHOUT_CHILD_ID );
      childInventory.setConditionCode( null );

      Inventory updatedChild = inventoryResourceBean.update( CHILD2_ID, childInventory, null );

      assertEquals( "child parent wasn't modified", PARENT_WITHOUT_CHILD_ID,
            updatedChild.getParentId() );
      assertEquals( "child conditionCode is wasn't updated", INSRV,
            updatedChild.getConditionCode() );
   }


   @Test
   public void testAttachInventoryToNewParent() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD1_ID );
      childInventory.setParentId( PARENT_WITHOUT_CHILD_ID );

      Inventory updatedChild = inventoryResourceBean.update( CHILD1_ID, childInventory, null );

      assertEquals( "child parent wasn't modified", PARENT_WITHOUT_CHILD_ID,
            updatedChild.getParentId() );
      assertEquals( "child conditionCode is wasn't updated", INSRV,
            updatedChild.getConditionCode() );
   }


   @Test
   public void testDetachInventoryAllowDefaultCondition() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD1_ID );
      childInventory.setParentId( "" );
      childInventory.setConditionCode( null );

      Inventory updatedChild = inventoryResourceBean.update( CHILD1_ID, childInventory, null );

      assertNull( "child is still attached", updatedChild.getParentId() );
      assertEquals( "child conditionCode is wasn't updated correctly.", RFI,
            updatedChild.getConditionCode() );
   }


   // Need JPA hook
   public void testAttachINSPREQInventoryFails() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD3_ID );
      childInventory.setParentId( PARENT_WITHOUT_CHILD_ID );
      childInventory.setConditionCode( INSRV );

      exceptionRule.expect( AmApiBusinessException.class );
      exceptionRule.expectMessage( "MXERR-30159" );
      inventoryResourceBean.update( CHILD3_ID, childInventory, null );
   }


   @Test
   public void testSetAttachedInventoryINSPREQFails() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD1_ID );
      childInventory.setParentId( null );
      childInventory.setConditionCode( INSPREQ );

      exceptionRule.expect( AmApiBusinessException.class );
      exceptionRule.expectMessage( "MXERR-30303" );
      inventoryResourceBean.update( CHILD1_ID, childInventory, null );
   }


   @Test
   public void testSetAttachedInventoryRFIFails() throws Exception {
      Inventory childInventory = inventoryResourceBean.get( CHILD1_ID );
      childInventory.setParentId( null );
      childInventory.setConditionCode( RFI );

      exceptionRule.expect( AmApiBusinessException.class );
      exceptionRule.expectMessage( "MXERR-30352" );
      inventoryResourceBean.update( CHILD1_ID, childInventory, null );
   }

}
