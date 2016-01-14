package pl.sportywarsaw.models;

public class RegisterAccountModel {
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterAccountModel(String userName, String email, String password, String confirmPassword) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
