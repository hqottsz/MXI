package com.mxi.mx.web.rest.supplychain.inventorycount;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.security.Principal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.crypto.parameter.ParameterEncrypter;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * Integration unit tests for the behaviours of
 * {@link InventoryCountEndpoint#createTransfer(SecurityContext, String)}
 *
 * @author Libin Cai
 * @created April 15, 2019
 */
@RunWith( MockitoJUnitRunner.class )
public class InventoryCountEndpointCreateTransferTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Mock
   private SecurityContext mockSecurityContext;
   @Mock
   private Principal mockPrincipal;

   private static final String USERNAME = "USERNAME";

   private InventoryCountEndpoint invCountEndpoint;
   private ParameterEncrypter encrypter;
   private Response response;

   private LocationKey toLocationKey;
   private InventoryKey inventoryKey;
   private JSONObject parm;


   @Before
   public void before() throws Exception {

      // Create a user and have the security context return that user as the principle user.
      Domain.createHumanResource( hr -> hr.setUsername( USERNAME ) );

      // // Mock the security
      when( mockSecurityContext.getUserPrincipal() ).thenReturn( mockPrincipal );
      when( mockPrincipal.getName() ).thenReturn( USERNAME );

      GlobalParametersFake lConfigParms =
            new GlobalParametersFake( InventoryCountEndpoint.PARM_TYPE_MXCOMMONWEB );
      lConfigParms.setBoolean( InventoryCountEndpoint.PARM_NAME_ENCRYPT_PARAMETERS, true );
      GlobalParameters.setInstance( lConfigParms );

      encrypter = new ParameterEncrypter( USERNAME );

      invCountEndpoint = new InventoryCountEndpoint();

      final String TO_LOC_CD = "toLocCd";

      LocationKey supplyLocationKey = Domain.createLocation();

      toLocationKey = Domain.createLocation( loc -> {
         loc.setCode( TO_LOC_CD );
         loc.setSupplyLocation( supplyLocationKey );
      } );

      LocationKey invLocationKey = Domain.createLocation( loc -> {
         loc.setCode( "invLocCd" );
         loc.setSupplyLocation( supplyLocationKey );
      } );

      PartNoKey partNoKey = Domain.createPart( part -> {
         part.setCode( "PART_NO_OEM" );
      } );

      inventoryKey = Domain.createSerializedInventory( inv -> {
         inv.setPartNumber( partNoKey );
         inv.setCondition( RefInvCondKey.RFI );
         inv.setLocation( invLocationKey );
      } );

      String encryptedInvKey = encrypter.encrypt( InventoryCountEndpoint.INVENTORY_KEY,
            inventoryKey.toString(), true );

      parm = new JSONObject();
      parm.put( "inventoryKey", encryptedInvKey );
      parm.put( "toLocation", TO_LOC_CD );
   }


   @After
   public void teardown() {
      GlobalParameters.setInstance( InventoryCountEndpoint.PARM_TYPE_MXCOMMONWEB, null );
   }


   @Test
   public void test_GIVEN_ValidInventory_WHEN_CallApiToCreateTransfer_THEN_TransferCreated()
         throws Exception {

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      // Assert the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );

      String entityString = response.getEntity().toString();

      JSONObject resultObj = new JSONObject( entityString );

      // assert transfer created
      assertFalse(
            StringUtils.isBlank( resultObj.getString( InventoryCountEndpoint.TRANSFER_KEY ) ) );

      // assert inventory is moved to to-location
      assertEquals( toLocationKey, InvInvTable.findByPrimaryKey( inventoryKey ).getLocation() );
   }


   @Test
   public void test_GIVEN_NonRfiInventory_WHEN_CallApiToCreateTransfer_THEN_TransferNotCreated()
         throws Exception {

      InvInvTable invTable = InvInvTable.findByPrimaryKey( inventoryKey );
      invTable.setInvCond( RefInvCondKey.INREP );
      invTable.update();

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      assertTransferNotCreated();
   }


   @Test
   public void test_GIVEN_IssuedInventory_WHEN_CallApiToCreateTransfer_THEN_TransferNotCreated()
         throws Exception {

      InvInvTable invTable = InvInvTable.findByPrimaryKey( inventoryKey );
      invTable.setIssuedBool( true );
      invTable.update();

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      assertTransferNotCreated();
   }


   @Test
   public void test_GIVEN_BatchInventory_WHEN_CallApiToCreateTransfer_THEN_TransferNotCreated()
         throws Exception {

      InvInvTable invTable = InvInvTable.findByPrimaryKey( inventoryKey );
      invTable.setInvClass( RefInvClassKey.BATCH );
      invTable.update();

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      assertTransferNotCreated();
   }


   @Test
   public void test_GIVEN_InventoryInKit_WHEN_CallApiToCreateTransfer_THEN_TransferNotCreated()
         throws Exception {

      // create kit inventory with inventoryKey as its content
      Domain.createKitInventory( kitInv -> {
         kitInv.setPartNo( Domain.createPart() );
         kitInv.addKitContentInventory( inventoryKey );
      } );

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      assertTransferNotCreated();
   }


   @Test
   public void
         test_GIVEN_InventoryHasPendingTransfer_WHEN_CallApiToCreateTransfer_THEN_TransferNotCreated()
               throws Exception {

      TransferKey pendTransferKey = new InventoryTransferBuilder().withInventory( inventoryKey )
            .withStatus( RefEventStatusKey.LXPEND ).build();

      EvtInvTable lEvtInv = EvtInvTable.create( pendTransferKey );
      lEvtInv.setInventoryKey( inventoryKey );
      lEvtInv.setMainInvBool( true );
      lEvtInv.insert();

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      assertTransferNotCreated();
   }


   @Test
   public void
         test_GIVEN_InventoryLocAndToLocNotUnderSameSupplyLoc_WHEN_CallApiToCreateTransfer_THEN_TransferNotCreated()
               throws Exception {

      // Change toLocation's supply location
      InvLocTable toLocTable = InvLocTable.findByPrimaryKey( toLocationKey );
      toLocTable.setSupplyLoc( Domain.createLocation() );
      toLocTable.update();

      response = invCountEndpoint.createTransfer( mockSecurityContext, parm.toString() );

      assertTransferNotCreated();
   }


   private void assertTransferNotCreated() throws JSONException {

      // Assert the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );

      String entityString = response.getEntity().toString();

      JSONObject resultObj = new JSONObject( entityString );

      // assert transfer not created
      assertTrue(
            StringUtils.isBlank( resultObj.getString( InventoryCountEndpoint.TRANSFER_KEY ) ) );
   }

}
