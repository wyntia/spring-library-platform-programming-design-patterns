//start L2 Composite
package branch;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite component representing a collection of library branches.
 */

public class BranchGroup implements BranchComponent {
    private final String name;
    private final List<BranchComponent> children = new ArrayList<>();
    public BranchGroup(String name) { this.name = name; }
    @Override
    public String getName() { return name; }
    @Override
    public List<BranchComponent> getChildren() { return children; }
    @Override
    public void addChild(BranchComponent child) { children.add(child); }
    @Override
    public void removeChild(BranchComponent child) { children.remove(child); }
}
//end L2 Composite
