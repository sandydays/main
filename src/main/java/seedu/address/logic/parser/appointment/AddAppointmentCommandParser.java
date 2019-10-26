package seedu.address.logic.parser.appointment;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_END_DATE_AND_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_START_DATE_AND_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PATIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_DAYS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_MINUTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_MONTHS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_WEEKS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_YEARS;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.appointment.AddAppointmentCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.datetime.EndDateTime;
import seedu.address.model.datetime.RecurringDateTime;
import seedu.address.model.datetime.StartDateTime;

/**
 * Parses input arguments and creates a new AddAppointmentCommand object
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAppointmentCommand
     * and returns an AddAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAppointmentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PATIENT_INDEX,
                        PREFIX_APPOINTMENT_START_DATE_AND_TIME, PREFIX_APPOINTMENT_END_DATE_AND_TIME,
                        PREFIX_RECUR_YEARS, PREFIX_RECUR_MONTHS, PREFIX_RECUR_WEEKS, PREFIX_RECUR_DAYS,
                        PREFIX_RECUR_HOURS, PREFIX_RECUR_MINUTES, PREFIX_APPOINTMENT_DESCRIPTION);

        if (!arePrefixesPresent(argMultimap, PREFIX_PATIENT_INDEX, PREFIX_APPOINTMENT_START_DATE_AND_TIME,
                PREFIX_APPOINTMENT_END_DATE_AND_TIME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE));
        }

        StartDateTime startDateTime = ParserUtil.parseStartDateTime(argMultimap
                .getValue(PREFIX_APPOINTMENT_START_DATE_AND_TIME).get());
        EndDateTime endDateTime = ParserUtil.parseEndDateTime(argMultimap
                .getValue(PREFIX_APPOINTMENT_END_DATE_AND_TIME).get(), argMultimap
                .getValue(PREFIX_APPOINTMENT_START_DATE_AND_TIME).get());

        Long years = ParserUtil.parseFrequency(argMultimap.getValue(PREFIX_RECUR_YEARS).get());
        Long months = ParserUtil.parseFrequency(argMultimap.getValue(PREFIX_RECUR_MONTHS).get());
        Long weeks = ParserUtil.parseFrequency(argMultimap.getValue(PREFIX_RECUR_WEEKS).get());
        Long days = ParserUtil.parseFrequency(argMultimap.getValue(PREFIX_RECUR_DAYS).get());
        Long hours = ParserUtil.parseFrequency(argMultimap.getValue(PREFIX_RECUR_HOURS).get());
        Long minutes = ParserUtil.parseFrequency(argMultimap.getValue(PREFIX_RECUR_MINUTES).get());
        Long[] freqArray = {years, months, weeks, days, hours, minutes};
        RecurringDateTime frequency = new RecurringDateTime(freqArray);

        Index indexPatient = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PATIENT_INDEX).get());
        String description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_APPOINTMENT_DESCRIPTION).get());

        Appointment appointment = new Appointment(startDateTime, endDateTime, frequency, indexPatient, description);

        return new AddAppointmentCommand(appointment);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
