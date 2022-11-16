package user;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User createRandomUser() {
        return new User(
                RandomStringUtils.randomAlphanumeric(7) + "@yandex.ru",
                "Qwerty123",
                RandomStringUtils.randomAlphabetic(7)
        );
    }
}
