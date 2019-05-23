package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.RefIetmTypeKey;


/**
 * Domain entity for Technical Reference
 *
 */
public class TechnicalReference {

   private IetmDefinitionKey iIetm;
   private String iName;
   private String iDefaultContext;
   private String iTaskDefinitionContext;
   private String iTaskContext;
   private RefIetmTypeKey iIetmType;
   private String iDescription;
   private String iApplicabilityRange;
   private List<CarrierKey> iOperators = new ArrayList<CarrierKey>();


   public List<CarrierKey> getOperators() {
      return iOperators;
   }


   public void addOperator( CarrierKey aOperator ) {
      iOperators.add( aOperator );
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public String getDefaultContext() {
      return iDefaultContext;
   }


   public void setDefaultContext( String aDefaultContext ) {
      iDefaultContext = aDefaultContext;
   }


   public String getTaskDefinitionContext() {
      return iTaskDefinitionContext;
   }


   public void setTaskDefinitionContext( String aTaskDefinitionContext ) {
      iTaskDefinitionContext = aTaskDefinitionContext;
   }


   public String getTaskContext() {
      return iTaskContext;
   }


   public void setTaskContext( String aTaskContext ) {
      iTaskContext = aTaskContext;
   }


   public RefIetmTypeKey getIetmType() {
      return iIetmType;
   }


   public void setIetmType( RefIetmTypeKey aType ) {
      iIetmType = aType;
   }


   public String getDescription() {
      return iDescription;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public String getApplicabilityRange() {
      return iApplicabilityRange;
   }


   public void setApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;
   }


   public IetmDefinitionKey getIetm() {
      return iIetm;
   }


   public void setIetm( IetmDefinitionKey aIetm ) {
      iIetm = aIetm;
   }

}
