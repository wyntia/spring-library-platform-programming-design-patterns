package org.pollub.reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.reservation.state.ActiveReservationState;
import org.pollub.reservation.state.ReservationState;
import org.pollub.reservation.state.ReservationStateFactory;

/**
 * Entity representing a reservation.
 * Uses IDs instead of entity references for cross-service data.
 */
@Entity
@Table(name = "reservation_history")
@Data
@AllArgsConstructor
@Builder
//Lab1 - Prototype Start
public class ReservationHistory implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    //L6 State Pattern - transient because state is derived from status
    @Transient
    private ReservationState state;

    //L6 State Pattern methods

    public ReservationHistory() {
        this.status = ReservationStatus.ACTIVE;
        this.reservedAt = DateTimeProvider.getInstance().now();
        this.expiresAt = this.reservedAt.plusDays(3);
        this.state = ReservationStateFactory.createState(this.status);
    }

    @PostLoad
    public void initState() {
        this.state = ReservationStateFactory.createState(this.status);
    }

    /**
     * Get the current state object based on status
     */
    public ReservationState getState() {
        if (state == null) {
            state = ReservationStateFactory.createState(this.status);
        }
        return state;
    }

    /**
     * Set state and update status accordingly
     */

    public void setStatus(ReservationStatus status) {
        this.status = status;
        this.state = ReservationStateFactory.createState(status);
    }

    //Lab1 - Prototype 2 Start
    @Override
    public ReservationHistory clone() {
        try {
            ReservationHistory cloned = (ReservationHistory) super.clone();
            cloned.setId(null);
            cloned.setReservedAt(DateTimeProvider.getInstance().now());
            cloned.setExpiresAt(DateTimeProvider.getInstance().now().plusDays(3));
            cloned.setResolvedAt(null);
            cloned.setStatus(ReservationStatus.ACTIVE);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported for ReservationHistory", e);
        }
    }
    //Lab1 End Prototype 2

}
