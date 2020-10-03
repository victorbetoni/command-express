package net.threadly.commandapi;

public class CommandResult {
    private Result result;
    private String message;

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

    public CommandResult(String message, Result result){
        this.result = result;
        this.message = message;
    }

    public static Builder builder(){
        return new Builder();
    }

    public Result getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
