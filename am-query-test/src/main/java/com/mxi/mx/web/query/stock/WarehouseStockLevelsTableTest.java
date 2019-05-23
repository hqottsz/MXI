
package com.mxi.mx.web.query.stock;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * Query test for WarehouseStockLevelsTable.qrx
 *
 * @auth Libin Cai
 * @created October 2, 2018
 */
@RunWith( Parameterized.class )
public final class WarehouseStockLevelsTableTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private String iOwnerCd;
   private boolean iIgnoreOwnerBool;
   private RefStockLowActionKey iStockLowAction;
   private String iExpectedOwnerCd;
   private boolean iHasSupplier;
   private Double iMaxQt;


   public WarehouseStockLevelsTableTest(RefStockLowActionKey aStockLowAction, String aOwnerCd,
         boolean aIgnoreOwnerBool, String aExpectedOwnerCd, boolean aHasSupplier, Double aMaxQt) {

      iStockLowAction = aStockLowAction;
      iOwnerCd = aOwnerCd;
      iIgnoreOwnerBool = aIgnoreOwnerBool;
      iExpectedOwnerCd = aExpectedOwnerCd;
      iHasSupplier = aHasSupplier;
      iMaxQt = aMaxQt;
   }


   @Parameterized.Parameters
   public static Collection<Object[]> primeNumbers() {
      return Arrays.asList( new Object[][] {

            // GIVEN stock level with ignore owner boolean as false, the owner code is returned
            { RefStockLowActionKey.MANUAL, "ownercd", false, "ownercd", false, 3.0 },

            // GIVEN stock level with ignore owner boolean as true, the owner code is NOT returned
            { RefStockLowActionKey.DISTREQ, "ownercd", true, null, true, null },

      } );
   }


   @Test
   public void setupDataAndAssertTheResult() {

      // --------------------------------------------------------------------
      // prepare testing data
      // --------------------------------------------------------------------
      final String HUB_LOC_CODE = "HLOC_CD";
      final String HUB_LOC_NAME = "HLOC_NAME";
      LocationKey lHub = Domain.createLocation( location -> {
         location.setCode( HUB_LOC_CODE );
         location.setName( HUB_LOC_NAME );
         location.setType( RefLocTypeKey.SRVSTORE );
      } );

      final String WH_LOC_CODE = "WLOC_CD";
      final String WH_LOC_NAME = "WLOC_NAME";

      LocationKey lWarehouse = Domain.createLocation( location -> {
         location.setCode( WH_LOC_CODE );
         location.setName( WH_LOC_NAME );
         location.setType( RefLocTypeKey.SRVSTORE );
         if ( iHasSupplier ) {
            location.setHubLocation( lHub );
         }
      } );

      RefInvClassKey lInvClass = RefInvClassKey.BATCH;
      StockNoKey lBatchStockNo =
            new StockBuilder().withInvClass( lInvClass ).withQtyUnitKey( RefQtyUnitKey.EA ).build();

      OwnerKey lStockLevelOwner = Domain.createOwner( owner -> owner.setCode( iOwnerCd ) );

      final double REORDER_QTY = 2.0;
      final double MIN_REORDER_LEVEL = 1.0;

      new StockLevelBuilder( lWarehouse, lBatchStockNo, lStockLevelOwner )
            .withMinReorderLevel( MIN_REORDER_LEVEL ).withReorderQt( REORDER_QTY )
            .withMaxLevel( iMaxQt ).withStockLowAction( iStockLowAction )
            .withIgnoreOwnerBool( iIgnoreOwnerBool ).build();

      // --------------------------------------------------------------------
      // run query
      // --------------------------------------------------------------------

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lBatchStockNo, "aStockNoDbId", "aStockNoId" );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );

      // --------------------------------------------------------------------
      // assert result
      // --------------------------------------------------------------------

      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      assertEquals( WH_LOC_CODE + " (" + WH_LOC_NAME + ")", lQs.getString( "location_cd_name" ) );
      assertEquals( iExpectedOwnerCd, lQs.getString( "owner_cd" ) );
      assertEquals( iHasSupplier ? HUB_LOC_CODE + " (" + HUB_LOC_NAME + ")" : null,
            lQs.getString( "hub_loc_cd_name" ) );
      assertEquals( MIN_REORDER_LEVEL, lQs.getDouble( "min_reorder_qt" ), 0 );
      assertEquals( REORDER_QTY, lQs.getDouble( "reorder_qt" ), 0 );
      assertEquals( iMaxQt == null ? 0 : iMaxQt, lQs.getDouble( "max_qt" ), 0 );
      assertEquals( iStockLowAction, lQs.getString( "stock_low_actn_cd" ) );
   }

}
