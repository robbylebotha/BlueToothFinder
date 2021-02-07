package it.ads.app.bluetoothfinder;

public class DeviceData {
    String deviceName;
    String deviceID;
    String deviceType;

    public DeviceData(String deviceName, String deviceID, String deviceType){
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.deviceType = deviceType;

    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
