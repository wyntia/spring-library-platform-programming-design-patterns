package org.pollub.catalog.state;

import org.pollub.catalog.model.CopyStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

//L6 State Design Pattern - Factory for creating CopyState instances based on CopyStatus
/**
 * Factory for creating CopyState instances based on CopyStatus enum.
 * Ensures consistent state instantiation throughout the application.
 */
public class CopyStateFactory {

    //Lab4 OCP Start
    private static final Map<CopyStatus, Supplier<CopyState>> STATE_SUPPLIERS = createStateSuppliers();
    //Lab4 OCP End

    /**
     * Create appropriate CopyState based on CopyStatus
     */
    public static CopyState createState(CopyStatus status) {
        //Lab4 OCP Start
        if (status == null || !STATE_SUPPLIERS.containsKey(status)) {
            return STATE_SUPPLIERS.get(CopyStatus.AVAILABLE).get();
        }
        return STATE_SUPPLIERS.get(status).get();
        //Lab4 OCP End
    }

    //Lab4 OCP Start
    private static Map<CopyStatus, Supplier<CopyState>> createStateSuppliers() {
        Map<CopyStatus, Supplier<CopyState>> stateSuppliers = new EnumMap<>(CopyStatus.class);
        stateSuppliers.put(CopyStatus.AVAILABLE, AvailableCopyState::new);
        stateSuppliers.put(CopyStatus.RENTED, RentedCopyState::new);
        stateSuppliers.put(CopyStatus.RESERVED, ReservedCopyState::new);
        return stateSuppliers;
    }
    //Lab4 OCP End
}

