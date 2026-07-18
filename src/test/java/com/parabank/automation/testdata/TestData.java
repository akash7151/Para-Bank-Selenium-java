package com.parabank.automation.testdata;

import com.parabank.automation.utilities.RandomDataUtility;

public final class TestData {

    private String firstName = "John";
    private String lastName = "Doe";
    private String address = "123 Main St";
    private String city = "Boston";
    private String state = "MA";
    private String zipCode = "02108";
    private String phone = RandomDataUtility.generatePhoneNumber();
    private String ssn = RandomDataUtility.generateSsn();
    private String username = RandomDataUtility.generateUsername();
    private String password = "password123";
    private String confirmPassword = "password123";

    private TestData() {
    }

    public static TestData builder() {
        return new TestData();
    }

    public TestData firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TestData lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TestData address(String address) {
        this.address = address;
        return this;
    }

    public TestData city(String city) {
        this.city = city;
        return this;
    }

    public TestData state(String state) {
        this.state = state;
        return this;
    }

    public TestData zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public TestData phone(String phone) {
        this.phone = phone;
        return this;
    }

    public TestData ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public TestData username(String username) {
        this.username = username;
        return this;
    }

    public TestData password(String password) {
        this.password = password;
        return this;
    }

    public TestData confirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getPhone() { return phone; }
    public String getSsn() { return ssn; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
}
