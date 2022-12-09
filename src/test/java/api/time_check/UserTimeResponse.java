package api.time_check;

public class UserTimeResponse extends UserTime {
    private  String updatedAt;

    public UserTimeResponse(){
        super();
    }

    public UserTimeResponse(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
