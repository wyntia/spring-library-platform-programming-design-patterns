//start L2 Composite
package branch;

import java.util.List;

/**
 * Base component for the library's hierarchical structure.
 */
public interface BranchComponent {
    String getName();
    List<BranchComponent> getChildren();
    void addChild(BranchComponent child);
    void removeChild(BranchComponent child);
}
//end L2 Composite
