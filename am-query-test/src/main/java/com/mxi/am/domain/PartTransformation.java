package com.mxi.am.domain;

import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class PartTransformation {

   TaskTaskKey iTask;
   PartNoKey iOldPartNo;
   PartNoKey iNewPartNo;


   public PartTransformation(PartNoKey aOldPartNo, PartNoKey aNewPartNo) {
      iOldPartNo = aOldPartNo;
      iNewPartNo = aNewPartNo;
   }


   public TaskTaskKey getTask() {
      return iTask;
   }


   public void setTask( TaskTaskKey aTask ) {
      iTask = aTask;
   }


   public PartNoKey getOldPartNo() {
      return iOldPartNo;
   }


   public void setOldPartNo( PartNoKey aOldPartNo ) {
      iOldPartNo = aOldPartNo;
   }


   public PartNoKey getNewPartNo() {
      return iNewPartNo;
   }


   public void setNewPartNo( PartNoKey aNewPartNo ) {
      iNewPartNo = aNewPartNo;
   }
}
