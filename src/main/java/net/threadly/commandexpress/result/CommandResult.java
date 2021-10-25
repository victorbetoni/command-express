package net.threadly.commandexpress.result;

import javax.annotation.Nullable;
import java7.util.Optional;

public class CommandResult {
    private Optional<Result> result;
    private Optional<String> message;

    public static class Builder {
        private Result result;
        private String message;

        public Builder result(Result result){
            this.result = result;
            return this;
        }

        public Builder message(String message){
            this.message = message;
            return this;
        }

        public CommandResult build(){
            return new CommandResult(message, result);
        }
    }

    public CommandResult(@Nullable String message, @Nullable Result result){
        this.result = Optional.ofNullable(result);
        this.message = Optional.ofNullable(message);
    }

    public static Builder builder(){
        return new Builder();
    }

    public Optional<Result> getResult() {
        return result;
    }

    public Optional<String> getMessage() {
        return message;
    }
}
