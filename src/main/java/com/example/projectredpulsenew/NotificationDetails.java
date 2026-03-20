package com.example.projectredpulsenew;

public class NotificationDetails {

    private String clickerName;
    private String clickerBloodgroup;
    private String clickerDistrict;
    private String clickerNumber;
    private String clickerEmail;
    private String clickerPassword;
    private String clickerProfilePic;

    private String receiverEmail;
    private String receiverPassword;

    private String notiType;
    private long timestamp;   // store time in milliseconds

    // ✅ No-argument constructor (required for Gson)
    public NotificationDetails() {
    }

    // Parameterized constructor (optional)
    public NotificationDetails(String clickerName, String clickerBloodgroup, String clickerDistrict, String clickerNumber,
                               String clickerEmail, String clickerPassword, String clickerProfilePic,
                               String receiverEmail, String receiverPassword,
                               String notiType, long timestamp) {
        this.clickerName = clickerName;
        this.clickerBloodgroup = clickerBloodgroup;
        this.clickerDistrict = clickerDistrict;
        this.clickerNumber = clickerNumber;
        this.clickerEmail = clickerEmail;
        this.clickerPassword = clickerPassword;
        this.clickerProfilePic = clickerProfilePic;
        this.receiverEmail = receiverEmail;
        this.receiverPassword = receiverPassword;
        this.notiType = notiType;
        this.timestamp = timestamp;
    }

    // =================== Getters ===================
    public String getClickerName() { return clickerName; }
    public String getClickerBloodgroup() { return clickerBloodgroup; }
    public String getClickerDistrict() { return clickerDistrict; }
    public String getClickerNumber() { return clickerNumber; }
    public String getClickerEmail() { return clickerEmail; }
    public String getClickerPassword() { return clickerPassword; }
    public String getClickerProfilePic() { return clickerProfilePic; }
    public String getReceiverEmail() { return receiverEmail; }
    public String getReceiverPassword() { return receiverPassword; }
    public String getNotiType() { return notiType; }
    public long getTimestamp() { return timestamp; }

    // =================== Setters =================== (optional)
    public void setClickerName(String clickerName) { this.clickerName = clickerName; }
    public void setClickerBloodgroup(String clickerBloodgroup) { this.clickerBloodgroup = clickerBloodgroup; }
    public void setClickerDistrict(String clickerDistrict) { this.clickerDistrict = clickerDistrict; }
    public void setClickerNumber(String clickerNumber) { this.clickerNumber = clickerNumber; }
    public void setClickerEmail(String clickerEmail) { this.clickerEmail = clickerEmail; }
    public void setClickerPassword(String clickerPassword) { this.clickerPassword = clickerPassword; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
    public void setReceiverPassword(String receiverPassword) { this.receiverPassword = receiverPassword; }
    public void setNotiType(String notiType) { this.notiType = notiType; }
    public void setClickerPic(String clickerProfilePic) { this.clickerProfilePic = clickerProfilePic; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

}
