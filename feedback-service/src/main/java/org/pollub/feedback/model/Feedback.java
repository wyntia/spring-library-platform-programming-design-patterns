package org.pollub.feedback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.feedback.visitor.FeedbackVisitor;

/**
 * Entity representing a user feedback submission.
 * Uses userId instead of User entity reference.
 */
@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackCategory category;
    
    @Column(length = 2000, nullable = false)
    private String message;
    
    @Column(length = 500)
    private String pageUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FeedbackStatus status = FeedbackStatus.NEW;
    
    /**
     * IP address for rate limiting
     */
    @Column(length = 45)
    private String ipAddress;
    
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = DateTimeProvider.getInstance().now();
    
    private LocalDateTime resolvedAt;

    /**
     * Accept method for the Visitor pattern to perform operations on the entity.
     */
    public void accept(FeedbackVisitor visitor) {
        visitor.visit(this); // start L6 Visitor
    }
}
