package com.mxi.mx.web.query.todolist;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * A Fleet Due List Argument Builder allowing us to easily create a defaulted or custom argument
 * list for the FleetDueList query.
 *
 */
public class DataSetArgumentBuilder {

   private int iHrDbId = 4650;
   private int iHrId = 1;
   private int iDayCount = 60;
   private boolean iIncludeAssignedToChecks = true;
   private boolean iHideNonExcecutableTasks = true;
   private List<String> iAssembliesToInclude = new ArrayList<String>();
   private boolean iSoftDeadline = false;
   private List<String> iGroupsToInclude = new ArrayList<String>();


   public DataSetArgument build() {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", iHrDbId );
      lArgs.add( "aHrId", iHrId );
      lArgs.add( "aDayCount", iDayCount );
      lArgs.add( "aIncludeAssignedToChecks", iIncludeAssignedToChecks );
      lArgs.add( "aHideNonExcecutableTasks", iHideNonExcecutableTasks );
      lArgs.add( "aSoftDeadline", iSoftDeadline );

      if ( iAssembliesToInclude.isEmpty() ) {
         iAssembliesToInclude.add( new String( "0" ) );
         lArgs.addStringArray( "aAssmblCds", iAssembliesToInclude );
      } else {
         lArgs.addStringArray( "aAssmblCds", iAssembliesToInclude );
      }
      if ( iGroupsToInclude.isEmpty() ) {
         iGroupsToInclude.add( new String( "0" ) );
         lArgs.addStringArray( "aAcftGroups", iGroupsToInclude );
      } else {
         lArgs.addStringArray( "aAcftGroups", iGroupsToInclude );
      }
      return lArgs;
   }


   public DataSetArgumentBuilder hrId( int aHrId ) {
      iHrId = aHrId;
      return this;
   }


   public DataSetArgumentBuilder dayCount( int aDayCount ) {
      iDayCount = aDayCount;
      return this;
   }


   public DataSetArgumentBuilder includeAssignedToChecks( boolean aIncludeAssignedToChecks ) {
      iIncludeAssignedToChecks = aIncludeAssignedToChecks;
      return this;
   }


   public DataSetArgumentBuilder assembliesToInclude( List<String> aAssembliesToInclude ) {
      iAssembliesToInclude = aAssembliesToInclude;
      return this;
   }


   public DataSetArgumentBuilder softDeadline( boolean aSoftDeadline ) {
      iSoftDeadline = aSoftDeadline;
      return this;
   };


   public DataSetArgumentBuilder groupsToInclude( List<String> aGroupsToInclude ) {
      iGroupsToInclude = aGroupsToInclude;
      return this;
   }

}
