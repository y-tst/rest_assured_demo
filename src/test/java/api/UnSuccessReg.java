package api;

public class UnSuccessReg {
    private String error;

    public UnSuccessReg() {
        super();
    }

    public UnSuccessReg(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
