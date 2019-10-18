package seedu.address.model.visit;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.person.Person;
import seedu.address.model.datetime.EndDateTime;
import seedu.address.model.datetime.StartDateTime;
import seedu.address.model.visittask.VisitTask;

/**
 * Represents a Visit in the application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Visit {
    // Pointer fields (not actually stored as a variable in JSON or CSV files)
    private final Person patient;

    // Data fields
    private final Remark remark;
    private final StartDateTime startDateTime;
    private final EndDateTime endDateTime;
    private final List<VisitTask> visitTasks;

    /**
     * Every field must be present and not null, with the exception of endDateTime
     * to accommodate for visits that have not finished.
     */
    public Visit(Remark remark, StartDateTime startDateTime,
                 EndDateTime endDateTime, List<VisitTask> visitTasks, Person patient) {
        requireAllNonNull(remark, startDateTime, visitTasks, patient);
        this.remark = remark;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visitTasks = visitTasks;
        this.patient = patient;
    }

    public Remark getRemark() {
        return remark;
    }

    public StartDateTime getStartDateTime() {
        return startDateTime;
    }

    public Optional<EndDateTime> getEndDateTime() {
        return endDateTime == null ? Optional.empty() : Optional.of(endDateTime);
    }

    /**
     * Returns an immutable visit task list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<VisitTask> getVisitTasks() {
        return Collections.unmodifiableList(visitTasks);
    }

    public Person getPatient() {
        return patient;
    }

    /**
     * Returns true if both visits have the same data fields and visit tasks.
     * This defines a stronger notion of equality between two visits.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Visit)) {
            return false;
        }

        Visit otherVisit = (Visit) other;
        return otherVisit.getRemark().equals(getRemark())
                && otherVisit.getStartDateTime().equals(getStartDateTime())
                && otherVisit.getEndDateTime().equals(getEndDateTime())
                && CollectionUtil.checkEqual(otherVisit.getVisitTasks(), getVisitTasks());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(remark, startDateTime, endDateTime, visitTasks, patient);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Start Time: ")
                .append(getStartDateTime())
                .append(" End Time: ");
        Optional<EndDateTime> endDateTime = getEndDateTime();
        builder.append(endDateTime.equals(Optional.empty()) ? "Ongoing" : endDateTime)
                .append(" Tags: ");
        getVisitTasks().forEach(builder::append);
        builder.append(" Remarks")
                .append(getRemark());
        return builder.toString();
    }

}
