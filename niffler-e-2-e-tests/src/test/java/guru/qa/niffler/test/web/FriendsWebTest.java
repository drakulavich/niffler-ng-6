package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

public class FriendsWebTest extends BaseWebTest {

  @User(friends = 2)
  @ApiLogin
  @Test
  void friendShouldBePresentInFriendsTable(UserJson user) {
    Selenide.open(FriendsPage.URL, FriendsPage.class)
      .checkThatFriendsTableContainsFriends(user.testData().friends());
  }

  @User
  @ApiLogin
  @Test
  void friendsTableShouldBeEmptyForNewUser(UserJson user) {
    Selenide.open(FriendsPage.URL, FriendsPage.class)
      .getPeopleTable().checkEmpty();
  }

  @User(income = 2)
  @ApiLogin
  @Test
  void incomeInvitationBePresentInFriendsTable(UserJson user) {
    Selenide.open(FriendsPage.URL, FriendsPage.class)
      .checkThatFriendRequestsTableContainsFriends(user.testData().income());
  }

  @User(outcome = 3)
  @ApiLogin
  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
    Selenide.open(PeoplePage.URL, PeoplePage.class)
      .checkOutcomeRequests(user.testData().outcome());
  }

  @User(income = 2)
  @ApiLogin
  @Test
  void userAcceptsIncomeInvitation(UserJson user) {
    String incomeUser = user.testData().income().getFirst();

    Selenide.open(FriendsPage.URL, FriendsPage.class)
      .getPeopleTable().acceptFriendRequest(incomeUser)
      .getPeopleTable().checkFriend(incomeUser);
  }

  @User(income = 2)
  @ApiLogin
  @Test
  void userRejectsIncomeInvitation(UserJson user) {
    String incomeUser = user.testData().income().getFirst();

    Selenide.open(FriendsPage.URL, FriendsPage.class)
      .getPeopleTable().declineFriendRequest(incomeUser)
      .checkAlert("Invitation of %s is declined".formatted(incomeUser));
  }
}
