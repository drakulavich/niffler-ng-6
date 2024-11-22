package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest extends BaseWebTest {

    @User(friends = 2)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().toFriendsPage()
                .checkThatFriendsTableContainsFriends(user.testData().friends());
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().toFriendsPage()
                .getPeopleTable().checkEmpty();
    }

    @User(income = 2)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().toFriendsPage()
                .checkThatFriendRequestsTableContainsFriends(user.testData().income());
    }

    @User(outcome = 3)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().toAllPeoplesPage()
                .checkOutcomeRequests(user.testData().outcome());
    }

    @User(income = 2)
    @Test
    void userAcceptsIncomeInvitation(UserJson user) {
        String incomeUser = user.testData().income().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().toFriendsPage()
                .getPeopleTable().acceptFriendRequest(incomeUser)
                .getPeopleTable().checkFriend(incomeUser);
    }

  @User(income = 2)
  @Test
  void userRejectsIncomeInvitation(UserJson user) {
    String incomeUser = user.testData().income().getFirst();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password())
      .getHeader().toFriendsPage()
      .getPeopleTable().declineFriendRequest(incomeUser)
      .checkAlert("Invitation of %s is declined".formatted(incomeUser));
  }
}
