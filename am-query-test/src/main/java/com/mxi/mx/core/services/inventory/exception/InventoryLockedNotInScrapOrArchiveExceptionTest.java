package com.mxi.mx.core.services.inventory.exception;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;


@RunWith( Parameterized.class )
public class InventoryLockedNotInScrapOrArchiveExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private RefInvClassKey iRefInvClassKey;
   private RefInvCondKey iInvCondition;
   private Boolean iExpectedResult;
   private Boolean iActualResult = false;


   public InventoryLockedNotInScrapOrArchiveExceptionTest(RefInvClassKey aInvRefClassKey,
         RefInvCondKey aInvCondition, Boolean aExpectedResult) {
      iRefInvClassKey = aInvRefClassKey;
      iInvCondition = aInvCondition;
      iExpectedResult = aExpectedResult;
   }


   @Parameterized.Parameters
   public static Collection<Object[]> testScenarios() {
      return Arrays.asList( new Object[][] { { RefInvClassKey.SER, RefInvCondKey.SCRAP, false },
            { RefInvClassKey.SER, RefInvCondKey.INSRV, true },
            { RefInvClassKey.SER, RefInvCondKey.ARCHIVE, false },
            { RefInvClassKey.SER, RefInvCondKey.QUAR, true },
            { RefInvClassKey.BATCH, RefInvCondKey.SCRAP, false },
            { RefInvClassKey.BATCH, RefInvCondKey.INSRV, true },
            { RefInvClassKey.BATCH, RefInvCondKey.ARCHIVE, false },
            { RefInvClassKey.BATCH, RefInvCondKey.QUAR, true },
            { RefInvClassKey.TRK, RefInvCondKey.SCRAP, false },
            { RefInvClassKey.TRK, RefInvCondKey.INSRV, true },
            { RefInvClassKey.TRK, RefInvCondKey.ARCHIVE, false },
            { RefInvClassKey.TRK, RefInvCondKey.QUAR, true } } );
   }


   /**
    *
    * Test if there are inventories which are locked and are not in SCRAP or ARCHIVE conditions.
    *
    */
   @Test
   public void testInvenotryNotInScrapOrArchiveLockedExist() {
      final PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( iRefInvClassKey ).build();
      new InventoryBuilder().withPartNo( lPartNo ).withCondition( iInvCondition ).isLocked()
            .build();

      try {
         InventoryLockedNotInScrapOrArchiveException.validate( lPartNo );
      } catch ( InventoryLockedNotInScrapOrArchiveException e ) {
         iActualResult = true;
      }

      assertTrue( "InventoryNotInScrapOrArchiveLockedException returns with part type "
            .concat( iRefInvClassKey.toString() ).concat( " with inventory condition " )
            .concat( iInvCondition.toString() ), iActualResult == iExpectedResult );

   }
}
