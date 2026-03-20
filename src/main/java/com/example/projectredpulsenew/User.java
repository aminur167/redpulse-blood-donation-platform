package com.example.projectredpulsenew;

public class User {
    private String name;
    private String age;
    private String gender;
    private String bloodGroup;
    private String district;
    private String email;
    private String password;
    private String Number;

    // ================= NEW FILE PATH FIELDS =================
    private String profilePicPath;
    private String medicalPdfPath;
    private String nidFrontPath;
    private String nidBackPath;

    // ================= NEW FIELDS =================
    private String lastDonateDate; // Format: yyyy-MM-dd
    private String nextDonateDate; // Format: yyyy-MM-dd
    private String eligibility;
    private boolean isAdmin = false;
    private boolean isModerator = false;




    public User() {
    }

    public User(String name, String age, String gender, String bloodGroup, String district, String Number, String email, String password, String profilePicPath, String medicalPdfPath, String nidFrontPath, String nidBackPath) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.Number = Number;
        this.email = email;
        this.password = password;
        this.profilePicPath = profilePicPath;
        this.medicalPdfPath = medicalPdfPath;
        this.nidFrontPath = nidFrontPath;
        this.nidBackPath = nidBackPath;
        this.eligibility = "Eligible";  // default value
        this.isAdmin = false;
        this.isModerator = false;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getNumber() { return Number; }
    public void setNumber(String Number) { this.Number = Number; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


    // ================= NEW FILE PATH GETTERS & SETTERS =================
    public String getProfilePicPath() { return profilePicPath; }
    public void setProfilePicPath(String profilePicPath) { this.profilePicPath = profilePicPath; }

    public String getMedicalPdfPath() { return medicalPdfPath; }
    public void setMedicalPdfPath(String medicalPdfPath) { this.medicalPdfPath = medicalPdfPath; }

    public String getNidFrontPath() { return nidFrontPath; }
    public void setNidFrontPath(String nidFrontPath) { this.nidFrontPath = nidFrontPath; }

    public String getNidBackPath() { return nidBackPath; }
    public void setNidBackPath(String nidBackPath) { this.nidBackPath = nidBackPath; }

    // ================= NEW GETTERS & SETTERS =================
    public String getLastDonateDate() {
        return lastDonateDate;
    }
    public void setLastDonateDate(String lastDonateDate) {
        this.lastDonateDate = lastDonateDate;
    }

    public String getNextDonateDate() {return nextDonateDate;}
    public void setNextDonateDate(String nextDonateDate) {this.nextDonateDate = nextDonateDate;}

    public String getEligibility() {
        return eligibility;
    }
    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { this.isAdmin = admin; }

    public boolean isModerator() { return isModerator; }
    public void setModerator(boolean isModerator) { this.isModerator = isModerator; }

}

