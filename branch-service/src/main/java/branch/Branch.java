//start L2 Composite
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
    //Lab5 : Liskov 2 Start
    @Override
    public boolean addChild(BranchComponent child) {
        // Zamiast rzucać UnsupportedOperationException, bezpiecznie ignorujemy próbę dodania
        return false;
    }
    @Override
    public boolean removeChild(BranchComponent child) {
        return false;
    }
    //Lab5 : Liskov 2 End
}
//end L2 Composite
