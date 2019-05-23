package com.mxi.mx.web.query.transfer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvKitMapKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefXferTypeKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.inv.InvKitMapTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Testing query com.mxi.mx.web.query.transfer.GetSubInvIssueXfersByPartRequest.qrx
 *
 * @author Frank Zhang
 * @created Feb 2, 2019
 *
 */
public class GetSubInvIssueXfersByPartRequestTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private double QTY_ONE = 1.0;
   private double QTY_TEN = 10.0;

   private InventoryKey iInventory;
   private InventoryKey iKitInventory;
   private PartRequestKey iKitPartRequest;

   /**
    *
    * GIVEN A kit inventory with SER content that is related to two part groups
    * WHEN  execute query GetSubInvIssueXfersByPartRequest.qrx
    * THEN  only one transfer for the SER content will be returned ( rather than two duplicates )
    *
    */
   @Test
   public void test_GIVEN_SerPartForTwoPartGroups_THEN_NoDuplicateTransfersReturned() {

      // setup kit with serialized content having SER part no in two part groups
      boolean lAnotherPartGroup = true;
      setupTransferData(  "SER Part", RefInvClassKey.SER, "Inv 001", QTY_ONE, QTY_ONE, lAnotherPartGroup );

      // execute query
      DataSet lDataSet = getSubInvIssueXfersByPartRequest( iKitPartRequest );

      // assert only one inventory transfer is returned ( rather than two duplicates )
      assertEquals( lDataSet.getRowCount(), 1 );
      lDataSet.next();
      assertEquals( lDataSet.getKey( InventoryKey.class, "sub_inv_key" ), iInventory );
   }


   /**
    *
    * GIVEN A kit inventory with batch content
    * AND   the kit inventory is issued with the batch content inventory removed from kit map
    * WHEN  execute query GetSubInvIssueXfersByPartRequest.qrx
    * THEN  transfer for the batch content will be returned ( rather than be ignored )
    *
     */
   @Test
   public void test_GIVEN_BatchKitContentPart_THEN_BatchInvTransferReturned() {

      // setup kit with batch content having batch part no just in one part group
      boolean lAnotherPartGroup = false;
      setupTransferData(  "BATCH Part", RefInvClassKey.BATCH, "Inv 001", QTY_TEN, QTY_ONE, lAnotherPartGroup );

      // batch inventory content is removed from kit map after kit inventory is issued
      InvKitMapKey lInvKitMapKey = new InvKitMapKey( iKitInventory, iInventory );
      InvKitMapTable lInvKitMapTable = InvKitMapTable.create( lInvKitMapKey );
      lInvKitMapTable.delete();

      // execute query
      DataSet lDataSet = getSubInvIssueXfersByPartRequest( iKitPartRequest );

      // assert batch inventory transfer is returned ( rather than be ignored )
      assertEquals( lDataSet.getRowCount(), 1 );
      lDataSet.next();
      assertEquals( lDataSet.getKey( InventoryKey.class, "sub_inv_key" ), iInventory );

   }


   /**
    * set up test data
    * iKitPartRequest and iInventoryare are created for calling the query and asserting the query result, respectively
    *
    * @param aPartName
    *       part name of the kit content
    * @param aInvClass
    *       inventory class for kit content
    * @param aInvNo
    *       Serial No or Batch No
    * @param aContentQty
    *       content qty in the kit
    * @param aRequestedQty
    *       requested kit qty
    *
    */
   private void setupTransferData( String aPartName, RefInvClassKey aInvClass, String aInvNo,
         double aContentQty, double aRequestedQty, boolean aAnotherPartGroup ) {
      // create part and part group
      PartNoKey lPart = Domain.createPart( aPart -> {
         aPart.setShortDescription( aPartName );
         aPart.setInventoryClass( aInvClass );
      } );

      PartGroupKey lPartGroupA = Domain.createPartGroup( aPartGroup -> {
         aPartGroup.setName( "Part Group A" );
         aPartGroup.setInventoryClass( aInvClass );
         aPartGroup.addPart( lPart );
      } );

      // create another part group for the part if needed
      if( aAnotherPartGroup ) {
         PartGroupKey lPartGroupB = Domain.createPartGroup( aPartGroup -> {
            aPartGroup.setName( "Part Group B" );
            aPartGroup.setInventoryClass( aInvClass );
            aPartGroup.addPart( lPart );
         } );
      }

      // create kit part with kit part group
      PartNoKey lKitPart = Domain.createPart( aPart -> {
         aPart.setShortDescription( "Kit Part" );
         aPart.setInventoryClass( RefInvClassKey.KIT );
      } );

      PartGroupKey lKitPartGroup = Domain.createPartGroup( aPartGroup -> {
         aPartGroup.setName( "Part Group KIT" );
         aPartGroup.setInventoryClass( RefInvClassKey.KIT );
         aPartGroup.addPart( lKitPart );
      } );

      // add part to kit as kit content
      new KitBuilder( lKitPart ).withContent( lPart, lPartGroupA ).withKitQuantity( aContentQty )
            .build();

      // create content inventory
      iInventory = new InventoryBuilder().withPartNo( lPart ).withSerialNo( aInvNo ).withClass( aInvClass ).withBinQt( aContentQty ).build();

      // create kit inventory with content inventory
      iKitInventory = Domain.createKitInventory( aKitInventory -> {
         aKitInventory.setPartNo( lKitPart );
         aKitInventory.setSerialNo( "KIT 001" );
         aKitInventory.addKitContentInventory( iInventory );
      } );

      // create part request on the kit part with reserved kit inventory
      iKitPartRequest = Domain.createPartRequest( aPartReq -> {
         aPartReq.partGroup( lKitPartGroup );
         aPartReq.specifiedPart( lKitPart );
         aPartReq.purchasePart( lKitPart );
         aPartReq.requestedQuantity( aRequestedQty );
         aPartReq.reservedInventory( iKitInventory );
      } );

      // create inventory transfers for kit inventory and it's content
      TransferKey lInvXfer = new InventoryTransferBuilder().withInventory( iInventory )
            .withKitInventory( iKitInventory ).withInitEvent( iKitPartRequest.getEventKey() )
            .withType( RefXferTypeKey.ISSUE ).withQuantity( new BigDecimal( 10 ) ).build();

      TransferKey lKitXfer = new InventoryTransferBuilder().withInventory( iKitInventory )
            .withInitEvent( iKitPartRequest.getEventKey() ).withType( RefXferTypeKey.ISSUE )
            .withQuantity( new BigDecimal( 1 ) ).build();
   }


   private DataSet getSubInvIssueXfersByPartRequest( PartRequestKey aPartReqKey ) {
      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartReqKey, new String[] { "aPartReqDbId", "aPartReqId" } );
      // execute query and return results
      DataSet lIds = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.transfer.GetSubInvIssueXfersByPartRequest", lArgs );
      return lIds;
   }

}
