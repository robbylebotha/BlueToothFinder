package it.ads.app.bluetoothfinder;

public class DeviceData {
    String deviceName;
    String deviceID;

    public DeviceData(String deviceName, String deviceID){
        this.deviceID = deviceID;
        this.deviceName = deviceName;

    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
