/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.hotel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author A00288569
 */
@XmlRootElement(name="hotelaccount")
// default ordering is alphabetic - we want the field order maintained
@XmlType(propOrder={"branchCode","accountNo","custName","custAddress","custType","custRating","bill"})
public class HotelAccount {
    private String branchCode,accountNo,
            custName, custAddress, custType,
            custRating;
    private double bill;
    
    public HotelAccount(){}
    
    public HotelAccount(String branchCode, String accountNo, String custName, String custAddress, String custType, String custRating, double bill) {
        this.branchCode = branchCode;
        this.accountNo = accountNo;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custType = custType;
        this.custRating = custRating;
        this.bill = bill;
    }
    
    @XmlElement
    public String getBranchCode() {
        return branchCode;
    }
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    @XmlElement
    public String getAccountNo() {
        return accountNo;
    }
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
     @XmlElement
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }

    @XmlElement
    public String getCustAddress() {
        return custAddress;
    }
    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    @XmlElement
    public String getCustType() {
        return custType;
    }
    public void setCustType(String custType) {
        this.custType = custType;
    }

    @XmlElement
    public String getCustRating() {
        return custRating;
    }
    public void setCustRating(String custRating) {
        this.custRating = custRating;
    }

    @XmlElement
    public double getBill() {
        return bill;
    }
    public void setBill(double bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "HotelAccount{" + "branchCode=" + branchCode + ", accountNo=" + accountNo + ", custName=" + custName + ", custAddress=" + custAddress + ", custType=" + custType + ", custRating=" + custRating + ", bill=" + bill + '}';
    }

    
    

}
