package graphcollection.exception;

import lombok.Getter;

public class GraphParseException extends RuntimeException {
    @Getter
    private final GraphParseError error;

    public GraphParseException(GraphParseError error) {
        super(error.getDescription());
        this.error = error;
    }

    public GraphParseException(GraphParseError error, Object... args) {
        super(error.getFormattedDescription(args));
        this.error = error;
    }
}