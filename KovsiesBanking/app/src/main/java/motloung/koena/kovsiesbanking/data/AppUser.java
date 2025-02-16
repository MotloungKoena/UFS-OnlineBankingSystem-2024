package motloung.koena.kovsiesbanking.data;

import androidx.annotation.NonNull;

import java.sql.ResultSet;

public class AppUser
{
    private String StudentNumber;
    private String FirstName;
    private String LastName;
    private String PassportIDNumber;
    private String Id;
    private String Username;
    private String NormalizedEmail;
    private String NormalizedUsername;
    private String MobilePassword;
    private String UserEmail;
    private String ProfilePicture;
    private String DateOfBirth;
    private String AccountNumber;
    private String PhysicalAddress;
    private String PhoneNumber;

    public AppUser(String studentNumber, String firstName, String lastName, String passportIDNumber,
                   String userEmail, String id, String dateOfBirth, String normalizedEmail,
                   String mobilePassword, String profilePicture, String accountNumber, String physicalAddress,
                   String phoneNumber, String normalizedUsername)
    {
        this.StudentNumber=studentNumber;
        this.FirstName=firstName;
        this.LastName=lastName;
        this.PassportIDNumber=passportIDNumber;
        this.UserEmail=userEmail;
        this.Id=id;
        this.DateOfBirth=dateOfBirth;
        this.NormalizedEmail = normalizedEmail;
        this.NormalizedUsername = normalizedUsername;
        this.MobilePassword = mobilePassword;
        this.ProfilePicture = profilePicture;
        this.AccountNumber = accountNumber;
        this.PhysicalAddress = physicalAddress;
        this.PhoneNumber = phoneNumber;
    }


    @NonNull
    @Override
    public String toString() {
        return FirstName + " " + LastName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
    public String getPhoneNumber(){return PhoneNumber;}
    public void setPhoneNumber(String phoneNumber){PhoneNumber=phoneNumber;}
    public String getPhysicalAddress(){return PhysicalAddress;}
    public void setPhysicalAddress(String physicalAddress){PhysicalAddress = physicalAddress;}
    public String getAccountNumber(){return AccountNumber;}
    public void setAccountNumber(String accountNumber){AccountNumber = accountNumber;}

    public String getDateOfBirth(){return DateOfBirth;}
    public void setDateOfBirth(String dBirth){DateOfBirth = dBirth;}
    public String getProfilePicture(){return ProfilePicture;}
    public void setProfilePicture(String sProfilePic){ProfilePicture = sProfilePic;}

    public String getUserEmail() {return UserEmail;}
    public void setUserEmail(String useMail) {UserEmail = useMail;}

    public String getMobilePassword(){return MobilePassword;}
    public  void setMobilePassword(String mPassword){MobilePassword = mPassword;}

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getIdNumber() {
        return PassportIDNumber;
    }

    public void setIdNumber(String idNumber) {
        PassportIDNumber = idNumber;
    }

    public String getStudentNumber() {
        return StudentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        StudentNumber = studentNumber;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
    public String getNormalizedUsername() {
        return NormalizedUsername;
    }

    public void setNormailizedUsername(String normalizedUsername){NormalizedUsername=normalizedUsername;}
    public String getNormalizedEmail() {
        return NormalizedEmail;
    }

    public void setNormalizedEmail(String normalizedEmail) {
        NormalizedEmail = normalizedEmail;
    }

    public static AppUser createUserFromResultSet(ResultSet set)
    {
        AppUser appUser = new AppUser(null,null,null,null,null,null,null,
                                        null,null,null,null,null,null,null);
        try {
            while (set.next())
            {
                appUser.setId(set.getString(1));
                appUser.setStudentNumber(set.getString(2));
                appUser.setFirstName(set.getString(3));
                appUser.setLastName(set.getString(4));
                appUser.setIdNumber(set.getString(5));
                appUser.setMobilePassword(set.getString(7));
                appUser.setDateOfBirth(set.getString(8));
                appUser.setProfilePicture(set.getString(9));
                appUser.setAccountNumber(set.getString(10));
                appUser.setPhysicalAddress(set.getString(11));
                appUser.setPhoneNumber(set.getString(12));
                appUser.setUsername(set.getString(15));
                appUser.setNormailizedUsername(set.getString(16));
                appUser.setUserEmail(set.getString(17));
                appUser.setNormalizedEmail(set.getString(18));
                break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return appUser;
    }
}
