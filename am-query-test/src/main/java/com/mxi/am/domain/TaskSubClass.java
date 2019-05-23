package com.mxi.am.domain;

import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;


public class TaskSubClass {

   private RefTaskSubclassKey id;
   private String name;
   private RefTaskClassKey taskClassKey;


   public RefTaskSubclassKey getId() {
      return id;
   }


   public void setId( RefTaskSubclassKey id ) {
      this.id = id;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }


   public RefTaskClassKey getTaskClassKey() {
      return taskClassKey;
   }


   public void setTaskClassKey( RefTaskClassKey taskClassKey ) {
      this.taskClassKey = taskClassKey;
   }

}
