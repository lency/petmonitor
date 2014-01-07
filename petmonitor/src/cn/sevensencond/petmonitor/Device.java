package cn.sevensencond.petmonitor;

public class Device
{
  public double balance = 0.0D;
  public double bdFenceLat = 0.0D;
  public double bdFenceLon = 0.0D;
  public int chargeMode = 2;
  public int chargePeriodType = 1;
  public String controlNumber = "";
  public int countPerPackage = 1;
  public int disableStatus = 3;
  public boolean enterFenceAlarm = false;
  public double fenceLatitude = 0.0D;
  public double fenceLongitude = 0.0D;
  public boolean fenceOn = false;
  public double fenceRadius = 1000.0D;
  public String filePath = "";
  public boolean gpsEnabled = true;
  public String guardian1 = "";
  public String guardian2 = "";
  public String guardian3 = "";
  public String guardian4 = "";
  public int id;
  public int interval = 0;
  public boolean isElectronicFenceOn = false;
  public int mode = 1;
  public int moveDistance;
  public String name = "";
  public String ownerName = "";
  public int ownerType = 1;
  public String phoneNumber = "";
  public String serialNumber = "";
  public String serviceEndDate = "";
  public int serviceStatus = 1;
  public long status;
  public String stickerUrl = "";
  public int trackerID;
  public byte[] trackerSticker = null;
  public int trackerStickerVer = 0;
  public int trackerType = 1;
  public String trackerTypeDesc = "";
  public String user = "";
  public int warnings = 0;
  
  public Device() {
      
  }

  public void copyFrom(Device device)
  {
    this.id = device.id;
    this.trackerID = device.trackerID;
    this.user = device.user;
    this.name = device.name;
    this.phoneNumber = device.phoneNumber;
    this.serialNumber = device.serialNumber;
    this.controlNumber = device.controlNumber;
    this.guardian1 = device.guardian1;
    this.guardian2 = device.guardian2;
    this.guardian3 = device.guardian3;
    this.guardian4 = device.guardian4;
    this.filePath = device.filePath;
    this.trackerTypeDesc = device.trackerTypeDesc;
    this.chargeMode = device.chargeMode;
    this.serviceStatus = device.serviceStatus;
    this.chargePeriodType = device.chargePeriodType;
    this.serviceEndDate = device.serviceEndDate;
    this.balance = device.balance;
    this.trackerType = device.trackerType;
    this.warnings = device.warnings;
    this.status = device.status;
    this.moveDistance = device.moveDistance;
    this.fenceLatitude = device.fenceLatitude;
    this.fenceLongitude = device.fenceLongitude;
    this.bdFenceLat = device.bdFenceLat;
    this.bdFenceLon = device.bdFenceLon;
    this.fenceOn = device.fenceOn;
    this.fenceRadius = device.fenceRadius;
    this.enterFenceAlarm = device.enterFenceAlarm;
    this.mode = device.mode;
    this.interval = device.interval;
    this.countPerPackage = device.countPerPackage;
    this.trackerSticker = device.trackerSticker;
    this.trackerStickerVer = device.trackerStickerVer;
    this.isElectronicFenceOn = device.isElectronicFenceOn;
    this.disableStatus = device.disableStatus;
    this.gpsEnabled = device.gpsEnabled;
    this.ownerType = device.ownerType;
    this.ownerName = device.ownerName;
    this.stickerUrl = device.stickerUrl;
  }

  public void copyTo(Device device)
  {
    device.id = this.id;
    device.trackerID = this.trackerID;
    device.user = this.user;
    device.name = this.name;
    device.phoneNumber = this.phoneNumber;
    device.serialNumber = this.serialNumber;
    device.controlNumber = this.controlNumber;
    device.guardian1 = this.guardian1;
    device.guardian2 = this.guardian2;
    device.guardian3 = this.guardian3;
    device.guardian4 = this.guardian4;
    device.filePath = this.filePath;
    device.trackerTypeDesc = this.trackerTypeDesc;
    device.chargeMode = this.chargeMode;
    device.serviceStatus = this.serviceStatus;
    device.chargePeriodType = this.chargePeriodType;
    device.serviceEndDate = this.serviceEndDate;
    device.balance = this.balance;
    device.trackerType = this.trackerType;
    device.warnings = this.warnings;
    device.status = this.status;
    device.moveDistance = this.moveDistance;
    device.fenceLatitude = this.fenceLatitude;
    device.fenceLongitude = this.fenceLongitude;
    device.bdFenceLat = this.bdFenceLat;
    device.bdFenceLon = this.bdFenceLon;
    device.fenceOn = this.fenceOn;
    device.fenceRadius = this.fenceRadius;
    device.enterFenceAlarm = this.enterFenceAlarm;
    device.mode = this.mode;
    device.interval = this.interval;
    device.countPerPackage = this.countPerPackage;
    device.trackerSticker = this.trackerSticker;
    device.trackerStickerVer = this.trackerStickerVer;
    device.isElectronicFenceOn = this.isElectronicFenceOn;
    device.disableStatus = this.disableStatus;
    device.gpsEnabled = this.gpsEnabled;
    device.ownerType = this.ownerType;
    device.ownerName = this.ownerName;
    device.stickerUrl = this.stickerUrl;
  }
}
