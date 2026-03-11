package org.pollub.rental.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import org.pollub.rental.state.RentalState;
import org.pollub.rental.state.RentalStateFactory;
import org.pollub.rental.state.RentedState;

//Lab1 - Builder Start
/**
 * Entity representing a completed rental transaction.
 * Uses manual Builder pattern instead of Lombok @Builder.
 */
@Entity
@Table(name = "rental_history")
public class RentalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "rented_at", nullable = false)
    private LocalDateTime rentedAt;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "is_extended", nullable = false)
    private Boolean isExtended;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RentalStatus status;

    // L3 State Pattern - current runtime state (nie zapisujemy w DB)
    @Transient
    private RentalState state;

    public RentalHistory() {}

    private RentalHistory(Builder builder) {
        this.id = builder.id;
        this.itemId = builder.itemId;
        this.userId = builder.userId;
        this.branchId = builder.branchId;
        this.rentedAt = builder.rentedAt;
        this.dueDate = builder.dueDate;
        this.returnedAt = builder.returnedAt;
        this.isExtended = builder.isExtended;
        this.status = builder.status;
        // stan spójny z enum
        this.state = RentalStateFactory.createState(this.status);
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public Long getId() { return id; }
    public Long getItemId() { return itemId; }
    public Long getUserId() { return userId; }
    public Long getBranchId() { return branchId; }
    public LocalDateTime getRentedAt() { return rentedAt; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public Boolean getIsExtended() { return isExtended; }
    public RentalStatus getStatus() { return status; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public void setRentedAt(LocalDateTime rentedAt) { this.rentedAt = rentedAt; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setReturnedAt(LocalDateTime returnedAt) { this.returnedAt = returnedAt; }
    public void setIsExtended(Boolean isExtended) { this.isExtended = isExtended; }

    public void setStatus(RentalStatus status) {
        this.status = status;
        // aktualizujemy state po zmianie statusu
        this.state = RentalStateFactory.createState(status);
    }

    @Override
    public String toString() {
        return "RentalHistory{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", branchId=" + branchId +
                ", rentedAt=" + rentedAt +
                ", dueDate=" + dueDate +
                ", returnedAt=" + returnedAt +
                ", isExtended=" + isExtended +
                ", status=" + status +
                '}';
    }

    /**
     * Manual Builder for RentalHistory with fluent API.
     */
    public static class Builder {
        private Long id;
        private Long itemId;
        private Long userId;
        private Long branchId;
        private LocalDateTime rentedAt;
        private LocalDateTime dueDate;
        private LocalDateTime returnedAt;
        private Boolean isExtended = false;
        private RentalStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder itemId(Long itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder branchId(Long branchId) {
            this.branchId = branchId;
            return this;
        }

        public Builder rentedAt(LocalDateTime rentedAt) {
            this.rentedAt = rentedAt;
            return this;
        }

        public Builder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder returnedAt(LocalDateTime returnedAt) {
            this.returnedAt = returnedAt;
            return this;
        }

        public Builder isExtended(Boolean isExtended) {
            this.isExtended = isExtended;
            return this;
        }

        public Builder status(RentalStatus status) {
            this.status = status;
            return this;
        }

        public RentalHistory build() {
            return new RentalHistory(this);
        }
    }

    // State Pattern methods

    /**
     * Get the current state object based on status.
     */
    public RentalState getState() {
        if (state == null) {
            state = RentalStateFactory.createState(this.status);
        }
        return state;
    }

    /**
     * Set state and update status accordingly.
     */
    public void setState(RentalState newState) {
        this.state = newState;
        updateStatusFromState();
    }

    /**
     * Update status enum based on current state.
     */
    private void updateStatusFromState() {
        if (state != null) {
            String stateName = state.getStateName();
            try {
                this.status = RentalStatus.valueOf(stateName);
            } catch (IllegalArgumentException e) {
                // fallback: jeśli coś się rozjedzie, ustaw domyślne RENTED
                this.status = RentalStatus.RENTED;
                this.state = new RentedState();
            }
        }
    }
}
//Lab1 End Builder
