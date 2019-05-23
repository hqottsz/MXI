package com.mxi.mx.core.unittest.usgrec;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.UsageDefinition;
import com.mxi.am.domain.builder.UsageDefinitionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EqpDataSourceSpecKey;
import com.mxi.mx.core.key.InvalidKeyException;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.services.assembly.CalculatedParmTO;
import com.mxi.mx.core.services.usagedefn.CannotUnassignUsageParmFromDataSourceException;
import com.mxi.mx.core.services.usagedefn.InvalidCalcParmOperationException;
import com.mxi.mx.core.services.usgrec.UsageRecordService;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.mim.MimPartNumData;
import com.mxi.mx.core.unittest.table.inv.InvCurrUsage;


/**
 * This class tests UsageRecordService's synchronizeUsage function. After execution of the function
 * <b>inv_curr_usage</b> table should get new row unless parameters get unassigned.
 *
 *
 * Special note: this test is moved from testingendejb as part of the Epic IDE-159: testingendejb -
 * Test Suite Deprecation. Originally there was only one test consequently testing assigned and
 * unassigned cases and it was not clear why because the first part does not affect the second one.
 * Now, there are two cases: the first one deals with the assigned parameters and expects that usage
 * Increments after sync. The second case deletes all parameters and expects no changes after sync.
 *
 * @author vopoca
 *
 */
public class UsageRecordServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test creates Inventory (Aircraft) and there should not be any records in <b>inv_curr_usage</b>
    * After it calls <i>UsageRecordService.synchronizeUsage()</i> then it checks if
    * <b>inv_curr_usage</b> has new record.
    *
    * @throws Exception
    *            Throws in case of failure of test
    */
   @Test
   public void testSynchronizeUsageWithAssignUsageParm() throws Exception {

      /** usage defn key for assign usage parm */

      // Given an activated recurring requirement that is not based on any conditions
      // And the requirement is not managed by a maintenance program
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );

      UsageDefinition lUsageDefinition = new UsageDefinition();
      lUsageDefinition.setAssembly( lAircraftAssembly );

      UsageDefinitionKey lUsageDefinitionKey = UsageDefinitionBuilder.build( lUsageDefinition );
      UsageRecordServiceTest.assignUsageParm( lUsageDefinitionKey, DataTypeKey.CYCLES );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      /* Confirm that there are still 0 rows for given Inv */
      InvCurrUsage.assertUsageCount( lAircraft, 0 );

      /* Now synchronize the usage by executing SQL Package */
      UsageRecordService.synchronizeUsage();

      /* There should be 1 rows in inv_curr_usage after synchronization */
      InvCurrUsage.assertUsageCount( lAircraft, 1 );

   }


   /**
    * Test creates Inventory (Aircraft) and there should not be any records in <b>inv_curr_usage</b>
    * After it unassigns parameters it calls <i>UsageRecordService.synchronizeUsage()</i> then it
    * checks if <b>inv_curr_usage</b> has NO new record.
    *
    * @throws Exception
    *            Throws in case of unexpected error
    */
   @Test
   public void testSynchronizeUsageWithUnAssignUsageParm() throws Exception {

      // Given an activated recurring requirement that is not based on any conditions
      // And the requirement is not manged by a maintenance program
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAcftAssyWithAcftPart( lAircraftPart );

      UsageDefinition lUsageDefinition = new UsageDefinition();
      lUsageDefinition.setAssembly( lAircraftAssembly );

      /* Assign a new usage parm */
      UsageDefinitionKey lUsageDefinitionKey = UsageDefinitionBuilder.build( lUsageDefinition );
      UsageRecordServiceTest.unAssignUsageParm( lUsageDefinitionKey, DataTypeKey.CYCLES );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
         aAircraft.allowSynchronization();
      } );

      /* Confirm that there are still 0 rows for given Inv */
      InvCurrUsage.assertUsageCount( lAircraft, 0 );

      UsageRecordService.synchronizeUsage();

      /* Confirm that there are still 0 rows in inv_curr_usage for this inventory */
      InvCurrUsage.assertUsageCount( lAircraft, 0 );
   }


   /*
    * PRIVATE AREA ********************************************************
    */

   /**
    * Sets a new value for the usage parm order property.
    *
    * @param aUsageDefn
    *           the new value for the usage parm order property.
    * @param aDataType
    *           the new value for the usage parm order property.
    */
   private static void setUsageParmOrder( UsageDefinitionKey aUsageDefn, DataTypeKey[] aDataType ) {
      EqpDataSourceSpec lEqpDataSourceSpec = null;

      for ( int i = 0; i < aDataType.length; i++ ) {
         lEqpDataSourceSpec = EqpDataSourceSpec.findByPrimaryKey( aUsageDefn, aDataType[i] );
         lEqpDataSourceSpec.setDataOrd( i + 1 );
         lEqpDataSourceSpec.update();
      }
   }


   /**
    * Deletes all rows for DataType in MIM_PART_NUMDATA
    *
    * @param aUsageDefinitionKey
    * @param aCycles
    * @throws MxException
    */
   private static void unAssignUsageParm( UsageDefinitionKey aUsageDefn, DataTypeKey aDataType )
         throws MxException {

      // validate the keys
      InvalidKeyException.validate( "aUsageDefn", aUsageDefn );
      InvalidKeyException.validate( "aDataType", aDataType );

      // delete assigned usage parm.
      UsageRecordServiceTest.deleteAssignedDataTypes( aUsageDefn, aDataType );

      EqpDataSourceSpec lEqpDataSourceSpec =
            new EqpDataSourceSpec( new EqpDataSourceSpecKey( aUsageDefn, aDataType ) );

      lEqpDataSourceSpec.delete();

      // arrange the order.
      UsageRecordServiceTest.setUsageParmOrder( aUsageDefn, getAssignedUsageParms( aUsageDefn ) );
   }


   /**
    * Assigns usage parm to an usage defn.
    *
    * @param aUsageDefn
    *           a usage definition key
    * @param aDataType
    *           a data type key
    *
    * @throws MxException
    *            if the key is invalid.
    */
   private static void assignUsageParm( UsageDefinitionKey aUsageDefn, DataTypeKey aDataType )
         throws MxException {

      // validate the keys.
      InvalidKeyException.validate( "aUsageDefn", aUsageDefn );
      InvalidKeyException.validate( "aDataType", aDataType );
      InvalidCalcParmOperationException.validate( aDataType );

      EqpDataSourceSpec.create( aUsageDefn, aDataType );

      addUpdatePartNumData( aUsageDefn, aDataType, null );
   }


   /**
    * Returns the value of the assigned usage parms property.
    *
    * @param aUsageDefn
    *           a usage defn key.
    *
    * @return the value of the assigned usage parms property.
    */
   private static DataTypeKey[] getAssignedUsageParms( UsageDefinitionKey aUsageDefn ) {
      String[] lCols = { "data_type_db_id", "data_type_id", "data_ord" };

      // create the args
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aUsageDefn,
            new String[] { "assmbl_db_id", "assmbl_cd", "data_source_db_id", "data_source_cd" } );

      // run the query
      DataSet lDs = MxDataAccess.getInstance().executeQuery( lCols, "eqp_data_source_spec", lArgs );

      // build the return array
      DataTypeKey[] lReturn = new DataTypeKey[lDs.getRowCount()];

      if ( lDs.getRowCount() > 0 ) {

         lDs.setSort( "dsInteger( \"data_ord\" )", true );

         while ( lDs.next() ) {
            lReturn[lDs.getRowNumber() - 1] =
                  new DataTypeKey( lDs.getInt( lCols[0] ), lDs.getInt( lCols[1] ) );
         }
      }

      return lReturn;
   }


   /**
    * Deletes the data type while unassigning a usage parm
    *
    * @param aUsageDefn
    *           a usage definition key.
    * @param aDataType
    *           a data type key.
    *
    * @throws MxException
    *            if the usage parm cannot be unassigned.
    */
   private static void deleteAssignedDataTypes( UsageDefinitionKey aUsageDefn,
         DataTypeKey aDataType ) throws MxException {
      int lDataTypeDbId = 0;
      int lDataTypeId = 0;
      String lDataTypeDbId_str = "data_type_db_id";
      String lDataTypeId_str = "data_type_id";

      DataSetArgument lDataSetArgs = new DataSetArgument();
      lDataSetArgs.add( "aAssmblDbId", aUsageDefn.getAssmblDbId() );
      lDataSetArgs.add( "aAssmblCd", aUsageDefn.getAssmblCd() );
      lDataSetArgs.add( "aDataSourceDbId", aUsageDefn.getDsDbId() );
      lDataSetArgs.add( "aDataSourceCd", aUsageDefn.getDsCd() );
      lDataSetArgs.add( "aDataTypeDbId", aDataType.getDbId() );
      lDataSetArgs.add( "aDataTypeId", aDataType.getId() );

      QuerySet lDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.usagedefn.GetDataTypesToDelete", lDataSetArgs );

      while ( lDs.next() ) {

         lDataTypeDbId = lDs.getInt( lDataTypeDbId_str );
         lDataTypeId = lDs.getInt( lDataTypeId_str );

         CannotUnassignUsageParmFromDataSourceException.validate( aUsageDefn,
               new DataTypeKey( lDataTypeDbId, lDataTypeId ), aDataType );

         DataSetArgument lDeleteArgs = new DataSetArgument();
         lDeleteArgs.add( "assmbl_db_id", aUsageDefn.getAssmblDbId() );
         lDeleteArgs.add( "assmbl_cd", aUsageDefn.getAssmblCd() );
         lDeleteArgs.add( "assmbl_bom_id", lDs.getInt( "assmbl_bom_id" ) );
         lDeleteArgs.add( "data_type_db_id", lDataTypeDbId );
         lDeleteArgs.add( "data_type_id", lDataTypeId );

         MxDataAccess.getInstance().executeDelete( "MIM_PART_NUMDATA", lDeleteArgs );
      }
   }


   /**
    * Utility method for add/updating the mim_part_numdata table
    *
    * @param aUsageDefn
    *           the usage defn key.
    * @param aDataType
    *           the data type key.
    * @param aTO
    *           the calculated parm TO.
    */
   private static void addUpdatePartNumData( UsageDefinitionKey aUsageDefn, DataTypeKey aDataType,
         CalculatedParmTO aTO ) {
      MimPartNumDataKey lMimPartNumData =
            new MimPartNumDataKey( new ConfigSlotKey( aUsageDefn.getAssemblyKey().getDbId(),
                  aUsageDefn.getAssemblyKey().getCd(), 0 ), aDataType );

      if ( aTO == null ) {

         if ( !lMimPartNumData.isValid() ) {
            MimPartNumData.create( lMimPartNumData );
         }
      } else {

         MimPartNumData lMimPartNumDataRow = MimPartNumData.findByPrimaryKey( lMimPartNumData );
         lMimPartNumDataRow.setEqnDescription( aTO.getBasicCalculation() );
         lMimPartNumDataRow.update();
      }
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAcftAssy ) {
            aAcftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aRootConfigSlot ) {
                  aRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup aRootCsPartGroup ) {
                        aRootCsPartGroup.setInventoryClass( ACFT );
                        aRootCsPartGroup.addPart( aAircraftPart );
                     }
                  } );
               }
            } );
         }
      } );
   }
}
