package org.pollub.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pollub.catalog.state.CopyState;
import org.pollub.catalog.state.AvailableCopyState;
import org.pollub.catalog.state.CopyStateFactory;

import java.time.LocalDateTime;

/**
 * Represents a copy of a library item at a specific branch.
 * Each branch can have its own copy with independent rental status.
 * Implements State Pattern to manage copy status transitions.
 */
@Entity
@Table(name = "branch_inventory",
       uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "branch_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchInventory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CopyStatus status = CopyStatus.AVAILABLE;

    // State Pattern - transient because state is derived from status
    @Transient
    private CopyState state;

    // ...existing code...

    // Rental info (populated when status = RENTED)
    @Column(name = "rented_by_user_id")
    private Long rentedByUserId;

    @Column(name = "rented_at")
    private LocalDateTime rentedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "rent_extended")
    @Builder.Default
    private Boolean rentExtended = false;

    // Reservation info (populated when status = RESERVED)
    @Column(name = "reserved_by_user_id")
    private Long reservedByUserId;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "reservation_expires_at")
    private LocalDateTime reservationExpiresAt;

    // State Pattern methods

    /**
     * Get the current state object based on status
     */
    public CopyState getState() {
        if (state == null) {
            state = CopyStateFactory.createState(this.status);
        }
        return state;
    }

    /**
     * Set state and update status accordingly
     */
    public void setState(CopyState newState) {
        this.state = newState;
        updateStatusFromState();
    }

    /**
     * Update status enum based on current state
     */
    private void updateStatusFromState() {
        if (state != null) {
            String stateName = state.getStateName();
            try {
                this.status = CopyStatus.valueOf(stateName);
            } catch (IllegalArgumentException e) {
                // Fallback to AVAILABLE if state name doesn't match enum
                this.status = CopyStatus.AVAILABLE;
                this.state = new AvailableCopyState();
            }
        }
    }

}

