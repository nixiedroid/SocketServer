package com.nixiedroid.rpc.util.logger;

public enum Level {

    /**
     * A marker to indicate that all levels are enabled.
     * This level {@linkplain #getSeverity() severity} is
     * {@link Integer#MIN_VALUE}.
     */
    ALL(Integer.MIN_VALUE),
    /**
     * {@code TRACE} level: usually used to log diagnostic information.
     * This level {@linkplain #getSeverity() severity} is
     * {@code 400}.
     */
    TRACE(400),
    /**
     * {@code DEBUG} level: usually used to log debug information traces.
     * This level {@linkplain #getSeverity() severity} is
     * {@code 500}.
     */
    DEBUG(500),
    /**
     * {@code INFO} level: usually used to log information messages.
     * This level {@linkplain #getSeverity() severity} is
     * {@code 800}.
     */
    INFO(800),
    /**
     * {@code WARNING} level: usually used to log warning messages.
     * This level {@linkplain #getSeverity() severity} is
     * {@code 900}.
     */
    WARNING(900),
    /**
     * {@code ERROR} level: usually used to log error messages.
     * This level {@linkplain #getSeverity() severity} is
     * {@code 1000}.
     */
    ERROR(1000),
    /**
     * A marker to indicate that all levels are disabled.
     * This level {@linkplain #getSeverity() severity} is
     * {@link Integer#MAX_VALUE}.
     */
    OFF(Integer.MAX_VALUE);

    private final int severity;

    private Level(int severity) {
        this.severity = severity;
    }

    /**
     * Returns the name of this level.
     * @return this level {@linkplain #name()}.
     */
    public final String getName() {
        return name();
    }

    /**
     * Returns the severity of this level.
     * A higher severity means a more severe condition.
     * @return this level severity.
     */
    public final int getSeverity() {
        return severity;
    }
}