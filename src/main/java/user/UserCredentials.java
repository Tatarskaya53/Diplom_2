package user;
import lombok.Data;

@Data
public class UserCredentials {
    private String email;
    private String password;
    private String name;

    public UserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword(), user.getName());
    }
}
