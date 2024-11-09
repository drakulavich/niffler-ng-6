package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Order(Integer.MAX_VALUE)
public class LastApiTest {

    @User
    @Test
    void allUsersReturnsListWithSomeData(UserJson user) {
        UserDataApiClient userDataApiClient = new UserDataApiClient();
        List<UserJson> allUsers = userDataApiClient.allUsers(user.username(), null);

        assertThat(allUsers).isNotEmpty();
    }
}
