package org.pollub.feedback.command;

import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.service.IFeedbackService;
import org.pollub.feedback.model.dto.FeedbackAdminDto;
import org.pollub.feedback.memento.FeedbackMemento;

//start L3 Command
public class UpdateFeedbackStatusCommand implements Command<FeedbackAdminDto> {
    private final IFeedbackService feedbackService;
    private final Long id;
    private final FeedbackStatus status;
    //start L3 Memento
    private FeedbackMemento memento;
    //end L3 Memento

    public UpdateFeedbackStatusCommand(IFeedbackService feedbackService, Long id, FeedbackStatus status) {
        this.feedbackService = feedbackService;
        this.id = id;
        this.status = status;
    }

    @Override
    public FeedbackAdminDto execute() {
        //start L3 Memento
        Feedback before = feedbackService.updateStatus(id, null);
        this.memento = new FeedbackMemento(before);
        //end L3 Memento
        Feedback updated = feedbackService.updateStatus(id, status);
        return FeedbackAdminDto.fromEntity(updated);
    }

    //start L3 Memento
    public FeedbackMemento getMemento() {
        return memento;
    }
    //end L3 Memento
}
//end L3 Command
