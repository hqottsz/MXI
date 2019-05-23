
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocPartCountKey;


/**
 * Builds a <code>inv_loc_part_count</code> object
 */
public class PartCountBuilder implements DomainBuilder<InvLocPartCountKey> {

   private InvLocPartCountKey iPartCountKey;
   private HumanResourceKey iHr;
   private Date iLastCountDate;
   private Date iNextCountDate;
   private boolean iIsHistorical;


   public PartCountBuilder(InvLocPartCountKey aPartCountKey) {
      iPartCountKey = aPartCountKey;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public InvLocPartCountKey build() {
      DataSetArgument lInsertArgs = new DataSetArgument();
      lInsertArgs.add( iPartCountKey, new String[] { "loc_db_id", "loc_id", "part_no_db_id",
            "part_no_id", "part_count_id" } );
      lInsertArgs.add( iHr, new String[] { "hr_db_id", "hr_id" } );
      lInsertArgs.add( "last_count_dt", iLastCountDate );
      lInsertArgs.add( "next_count_dt", iNextCountDate );
      lInsertArgs.add( "hist_bool", iIsHistorical );
      MxDataAccess.getInstance().executeInsert( "inv_loc_part_count", lInsertArgs );

      return iPartCountKey;
   }


   /**
    * Sets a new value for the iPartCountKey property.
    *
    * @param aPartCountKey
    *           the new value for the iPartCountKey property
    */
   public PartCountBuilder withPartCountKey( InvLocPartCountKey aPartCountKey ) {
      this.iPartCountKey = aPartCountKey;

      return this;
   }


   /**
    * Sets a new value for the iHr property.
    *
    * @param aHr
    *           the new value for the iHr property
    */
   public PartCountBuilder withHr( HumanResourceKey aHr ) {
      this.iHr = aHr;

      return this;
   }


   /**
    * Sets a new value for the iLastCountDate property.
    *
    * @param aLastCountDate
    *           the new value for the iLastCountDate property
    */
   public PartCountBuilder withLastCountDate( Date aLastCountDate ) {
      this.iLastCountDate = aLastCountDate;

      return this;
   }


   /**
    * Sets a new value for the iNextCountDate property.
    *
    * @param aNextCountDate
    *           the new value for the iNextCountDate property
    */
   public PartCountBuilder withNextCountDate( Date aNextCountDate ) {
      this.iNextCountDate = aNextCountDate;

      return this;
   }


   /**
    * Sets a new value for the iIsHistorical property.
    *
    * @param aIsHistorical
    *           the new value for the iIsHistorical property
    */
   public PartCountBuilder isHistorical( boolean aIsHistorical ) {
      this.iIsHistorical = aIsHistorical;

      return this;
   }
}
