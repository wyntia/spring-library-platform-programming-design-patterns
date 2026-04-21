package org.pollub.common.function;

//Lab7 : Interfejsy funkcyjne i lambda — definicja 2/3
/**
 * Reports aggregated outcome of a rental reminder batch job (e.g. logging).
 */
@FunctionalInterface
public interface RentalReminderBatchReporter {

    void report(int successCount, int failureCount);
}
