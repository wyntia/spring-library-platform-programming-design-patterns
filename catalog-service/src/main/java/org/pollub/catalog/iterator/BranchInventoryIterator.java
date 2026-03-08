package org.pollub.catalog.iterator;

import org.pollub.catalog.model.BranchInventory;
import java.util.Iterator;
import java.util.List;

//start L5 Iterator
public class BranchInventoryIterator implements Iterator<BranchInventory> {
    private final List<BranchInventory> inventories;
    private int position = 0;

    public BranchInventoryIterator(List<BranchInventory> inventories) {
        this.inventories = inventories;
    }

    @Override
    public boolean hasNext() {
        return position < inventories.size();
    }

    @Override
    public BranchInventory next() {
        return inventories.get(position++);
    }
}
//end L5 Iterator
