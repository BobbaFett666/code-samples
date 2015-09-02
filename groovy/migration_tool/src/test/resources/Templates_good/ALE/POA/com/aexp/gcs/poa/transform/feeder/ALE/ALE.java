
package com.aexp.gcs.poa.transform.feeder.ALE;

import com.aexp.gcs.poa.transform.AbstractTransformation;


/**
 * This class will contain ALE feeder specific Transformation rule
 * 
 */
abstract class ALE
    extends AbstractTransformation
{


    @Override
    public String description() {
        return "This is ALE Feeder Specific Rule";
    }

}
