package motloung.koena.kovsiesbanking.adapters;

import java.io.Serializable;

public class UserData implements Serializable
{
    private String AccNumber;
    private String userEmail;
    private String userId;
    private String studentEmployeeNumber;
    private String NormalizedEmaill;
    private String FirstName;
    private String LastName;
    private String UserName;
    private String PhoneNumberr;


    public UserData(String userEmail, String Id, String normalEmail, String firstName, String accNumber, String studentEmployeeNumber, String lastName, String userName, String phoneNumber )
    {
        this.userEmail = userEmail; this.userId = Id; this.NormalizedEmaill=normalEmail; this.FirstName = firstName;
        this.AccNumber = accNumber; this.studentEmployeeNumber=studentEmployeeNumber; this.LastName = lastName;
        this.UserName=userName; this.PhoneNumberr = phoneNumber;
    }

    public  String getPhoneNumebr(){return  PhoneNumberr;}
    public void setPhoneNumberr(String phoneNumber) {this.PhoneNumberr = phoneNumber;}

    public  String getUserName(){return  UserName;}
    public void setUserName(String userName) {this.UserName = userName;}

    public  String getLastName(){return  LastName;}
    public void setLastName(String lastName) {this.LastName = lastName;}

    public  String getAccountNumber(){return  AccNumber;}
    public void setAccountNumber(String accountNumber) {this.AccNumber = accountNumber;}

    public  String getStudentEmployeeNumber(){return  studentEmployeeNumber;}
    public void setStudentEmployeeNumberId(String studentEmployeeNumber) {this.studentEmployeeNumber = studentEmployeeNumber;}

    public String getNormalizedEmail() {
        return NormalizedEmaill;
    }
    public void setnormalizedEmail(String normEmail) {
        this.NormalizedEmaill = normEmail;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }
}
