package edu.twister.malik.services.user;


public class SessionDescription {

    private int iid;
    private int useriid;
    private String username;

    private final static int _MAX_SESSION_DURATION = 7200; //2 hours

    public SessionDescription
        (int iid, int useriid, String username) {
            this.useriid = useriid;
            this.username = username;
            this.iid = iid;
        }

    public int
        getSessionIID() {
            return this.iid;
        }

    public String
        getUsername() {
            return this.username;
        }

    public int
        getUserIID() {
            return this.useriid;
        }

}
