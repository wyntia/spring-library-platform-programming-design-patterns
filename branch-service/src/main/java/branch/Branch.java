//start L3 Composite
package branch;

import java.util.Collections;
import java.util.List;

/**
    Composite Leaf – single library branch.
 */
public class Branch implements BranchComponent {
  
    private final String name;
    public Branch(String name) { this.name = name; }
    @Override
    public String getName() { return name; }
    @Override
    public List<BranchComponent> getChildren() { return Collections.emptyList(); }
    @Override
    public void addChild(BranchComponent child) {
        throw new UnsupportedOperationException("Branch (liść) nie obsługuje dzieci");
    }
    @Override
    public void removeChild(BranchComponent child) {
        throw new UnsupportedOperationException("Branch (liść) nie obsługuje dzieci");
    }
}
//end L3 Composite
