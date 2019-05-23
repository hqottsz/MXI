package com.mxi.mx.web.query.inventory;

import static com.mxi.mx.core.key.RefInvCondKey.INREP;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.ref.RefInvCondTable;


/**
 * Query unit test for InventoryDetails.qrx
 */
public class InventoryDetailsTest {

   @Rule
   public final FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public final DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   /**
    * Ensure the query returns the correct condition code name.
    */
   @Test
   public void returnsConditionCodeName() {
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setCondition( INREP );
         // Owner is mandatory for the query but not relevant to the test.
         acft.setOwner( Domain.createOwner() );
      } );

      QuerySet qs = executeQuery( aircraft );
      qs.next();
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected condition code name.", qs.getString( "cond_cd_name" ),
            is( buildExpectedConditionCodeName( INREP ) ) );
   }


   /**
    * Ensure the query returns the correct release date.
    */
   @Test
   public void returnsReleaseDate() {
      Date releaseDate = DateUtils.floorMillisecond( new Date() );
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setReleaseDate( releaseDate );
         // Condition and owner are mandatory for the query but not relevant to the test.
         acft.setCondition( INREP );
         acft.setOwner( Domain.createOwner() );
      } );

      QuerySet qs = executeQuery( aircraft );
      qs.next();
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected release date.", qs.getDate( "release_dt" ), is( releaseDate ) );
   }


   /**
    * Ensure the query returns the correct release number.
    */
   @Test
   public void returnsReleaseNumber() {
      String releaseNumber = "12345";
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setReleaseNumber( releaseNumber );
         // Condition and owner are mandatory for the query but not relevant to the test.
         acft.setCondition( INREP );
         acft.setOwner( Domain.createOwner() );
      } );

      QuerySet qs = executeQuery( aircraft );
      qs.next();
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected release number.", qs.getString( "release_number" ),
            is( releaseNumber ) );
   }


   /**
    * Ensure the query returns the correct release remarks.
    */
   @Test
   public void returnsReleaseRemarks() {
      String releaseRemarks = "releaseRemarks";
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setReleaseRemarks( releaseRemarks );
         // Condition and owner are mandatory for the query but not relevant to the test.
         acft.setCondition( INREP );
         acft.setOwner( Domain.createOwner() );
      } );

      QuerySet qs = executeQuery( aircraft );
      qs.next();
      assertThat( "Unexpected number of rows returned.", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected release remarks.", qs.getString( "release_remarks" ),
            is( releaseRemarks ) );
   }


   private String buildExpectedConditionCodeName( RefInvCondKey inventoryCondition ) {
      RefInvCondTable refInvCondTable = RefInvCondTable.findByPrimaryKey( inventoryCondition );
      return refInvCondTable.getUserCd() + " (" + refInvCondTable.getShortDescription() + ")";
   }


   private QuerySet executeQuery( InventoryKey inventory ) {
      DataSetArgument args = new DataSetArgument();
      args.add( inventory, "aInvNoDbId", "aInvNoId" );
      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );
   }

}
