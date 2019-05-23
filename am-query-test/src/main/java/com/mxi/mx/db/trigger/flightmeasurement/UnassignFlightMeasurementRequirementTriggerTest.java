package com.mxi.mx.db.trigger.flightmeasurement;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.ejb.assembly.AssemblyBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;


/**
 * This class tests the Flight Measurement Requirement Modified Triggers
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class UnassignFlightMeasurementRequirementTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;

   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( "4650:ACFTFL1" );
   private static final DataTypeKey DATA_TYPE_CD1 = new DataTypeKey( "4650:100006" );
   private static final DataTypeKey DATA_TYPE_CD2 = new DataTypeKey( "4650:100007" );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private AssemblyBean assemblyBean;


   @Before
   public void setupData() throws ParseException {

      assemblyBean = new AssemblyBean();
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );

      triggerCalled = false;
   }


   /**
    *
    * This Test will Assign a measurement and then Unassigns it to check if the trigger will be
    * fired in this event.
    *
    * @throws AmApiBusinessException
    * @throws SQLException
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testUnassignFlightMeasurementRequirementTrigger()
         throws AmApiBusinessException, SQLException, MxException, TriggerException {

      DataTypeKey[] dataTypeCodeList = new DataTypeKey[] { DATA_TYPE_CD1, DATA_TYPE_CD2 };
      // Assigns Measurement
      assemblyBean.assignMeasurement( ASSEMBLY_KEY, dataTypeCodeList );
      triggerCalled = false;
      // Unassigns Measurement for the Test
      assemblyBean.unassignMeasurement( ASSEMBLY_KEY, dataTypeCodeList );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    * This method gets called when the trigger is invoked {@inheritDoc}
    */
   @Override
   public void after( UUID assemblyId ) throws TriggerException {

      triggerCalled = true;
   }
}
