package com.mxi.am.domain.builder;

import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.CalculatedUsageParameter.CalculationInput;
import com.mxi.am.domain.CalculatedUsageParameter.Constant;
import com.mxi.am.domain.CalculatedUsageParameter.Parameter;
import com.mxi.am.domain.CalculatedUsageParameter.PartSpecificConstant;
import com.mxi.am.domain.Domain;
import com.mxi.mx.core.key.CalcInputKey;
import com.mxi.mx.core.key.CalcParmKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.table.mim.MimCalc;
import com.mxi.mx.core.table.mim.MimCalcInput;
import com.mxi.mx.core.table.mim.MimDataType;
import com.mxi.mx.core.table.mim.MimPartInput;
import com.mxi.mx.core.table.mim.MimPartNumData;


public class CalculatedUsageParameterBuilder {

   public static DataTypeKey build( CalculatedUsageParameter aCalculatedUsageParameter ) {

      // If the data type is provided then use it, otherwise create a new data type.
      DataTypeKey lDataType = aCalculatedUsageParameter.getDataType();
      if ( lDataType == null ) {
         lDataType = MimDataType.create( aCalculatedUsageParameter.getCode(),
               aCalculatedUsageParameter.getName(), RefDomainTypeKey.USAGE_PARM, null,
               aCalculatedUsageParameter.getPrecisionQt(), null );
      }

      ConfigSlotKey lConfigSlot = aCalculatedUsageParameter.getConfigurationSlot();

      // Create row in mim_part_numdata to map the data type to the config slot.
      MimPartNumData.create( new MimPartNumDataKey( lConfigSlot, lDataType ) );

      // Create new row in mim_calc to store the DB function.
      MimCalc lMimCalc = new MimCalc();
      lMimCalc.setAssembly( lConfigSlot.getAssemblyKey() );
      lMimCalc.setDataType( lDataType );
      lMimCalc.setEqnDescription( aCalculatedUsageParameter.getDatabaseCalculation() );
      CalcParmKey lCalcParmKey = lMimCalc.insert();

      // Create any input parameters for the function (both parameters and/or constants).
      for ( CalculationInput lCalculationInput : aCalculatedUsageParameter
            .getCalculationInputs() ) {

         if ( lCalculationInput instanceof Parameter ) {
            // Create a parameter.
            Parameter lParameter = ( Parameter ) lCalculationInput;
            MimCalcInput lCalcInput = new MimCalcInput();
            lCalcInput.setDataTypeKey( lParameter.iDataType );
            lCalcInput.setConstant( false );
            lCalcInput.setInputCd(
                  MimDataType.findByPrimaryKey( lParameter.iDataType ).getDataTypeCd() );
            lCalcInput.setInputOrdinal(
                  aCalculatedUsageParameter.getCalculationInputs().indexOf( lCalculationInput ) );
            lCalcInput.insert( lCalcParmKey );

         } else if ( lCalculationInput instanceof Constant ) {
            // Create a constant.
            Constant lConstant = ( Constant ) lCalculationInput;
            MimCalcInput lCalcInput = new MimCalcInput();
            lCalcInput.setConstant( true );
            lCalcInput.setInputCd( lConstant.iName );
            lCalcInput.setInputQuantity( lConstant.iValue.doubleValue() );
            lCalcInput.setInputOrdinal(
                  aCalculatedUsageParameter.getCalculationInputs().indexOf( lCalculationInput ) );
            CalcInputKey lCalcInputKey = lCalcInput.insert( lCalcParmKey );

            // Create any part specific constants for the newly created constant.
            //
            // Note: the part specific constant may apply to a config slot that is within the tree
            // under the calc. usage parm's config slot.
            //
            // Note: part specific constants do not map to a part but rather to a particular part
            // within a particular part group.
            //
            for ( PartSpecificConstant lPartSpecificConstant : aCalculatedUsageParameter
                  .getPartSpecificConstants() ) {

               // Given the part specific constant's config slot and part group code get the part
               // group key.
               ConfigSlotKey lPartSpecConstCs = Domain.readSubConfigurationSlot( lConfigSlot,
                     lPartSpecificConstant.iConfigurationSlotCode );
               PartGroupKey lPartSpecConstPartGroup =
                     Domain.readPartGroup( lPartSpecConstCs, lPartSpecificConstant.iPartGroupCode );

               EqpPartBaselineKey lPartWithinPartGroupKey = new EqpPartBaselineKey(
                     lPartSpecConstPartGroup, lPartSpecificConstant.iPartNumber );

               // Create part specific constant in mim_part_input
               MimPartInput lMimPartInput = new MimPartInput();
               lMimPartInput.insert( lCalcInputKey, lPartWithinPartGroupKey );
               lMimPartInput.setInputQuantity( lPartSpecificConstant.iValue.doubleValue() );
               lMimPartInput.update();

            }
         } else {
            throw new RuntimeException(
                  "Unsupported CalculationInput class: " + lCalculationInput.getClass().getName() );
         }
      }
      return lDataType;
   }

}
