//start L2 Composite
package branch;

import java.util.List;

/**
 * Base component for the library's hierarchical structure.
 */
public interface BranchComponent {
    String getName();
    List<BranchComponent> getChildren();
    //Lab5 : Liskov 2 Start
    boolean addChild(BranchComponent child);
    boolean removeChild(BranchComponent child);
    //Lab5 : Liskov 2 End
}
//end L2 Composite
