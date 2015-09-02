
package com.aexp.gcs.poa.transform.feeder.IAL;

import com.aexp.gcs.poa.transform.AbstractTransformation;


/**
 * This class will contain IAL feeder specific Transformation rule
 * 
 */
abstract class IAL
    extends AbstractTransformation
{


    @Override
    public String description() {
        return "This is IAL Feeder Specific Rule";
    }

}
