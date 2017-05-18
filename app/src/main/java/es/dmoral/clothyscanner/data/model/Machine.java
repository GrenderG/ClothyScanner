package es.dmoral.clothyscanner.data.model;

/**
 * Created by grender on 18/05/17.
 */

public class Machine {
    private String ip;
    private String friendlyName;
    private boolean active;

    public Machine() {
        // empty
    }

    public Machine(String ip, String friendlyName, boolean active) {
        this.ip = ip;
        this.friendlyName = friendlyName;
        this.active = active;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Machine && ((Machine) obj).getIp().equals(getIp());
    }
}
