package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.grpc.UserResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jaxb.userdata.Currency;
import jaxb.userdata.User;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFrom;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("fullname")
    String fullname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photo")
    String photo,
    @JsonProperty("photoSmall")
    String photoSmall,
    @JsonProperty("friendshipStatus")
    FriendshipStatus friendshipStatus) implements IUserJson {

  public @Nonnull User toJaxbUser() {
    User jaxbUser = new User();
    jaxbUser.setId(id != null ? id.toString() : null);
    jaxbUser.setUsername(username);
    jaxbUser.setFirstname(firstname);
    jaxbUser.setSurname(surname);
    jaxbUser.setFullname(fullname);
    jaxbUser.setCurrency(currency != null ? Currency.valueOf(currency.name()) : null);
    jaxbUser.setPhoto(photo);
    jaxbUser.setPhotoSmall(photoSmall);
    jaxbUser.setFriendshipStatus(friendshipStatus() == null ?
        jaxb.userdata.FriendshipStatus.VOID :
        jaxb.userdata.FriendshipStatus.valueOf(friendshipStatus().name()));
    return jaxbUser;
  }

  public @Nonnull UserResponse toGrpcUserResponse() {
    return UserResponse.newBuilder()
      .setId(id.toString())
      .setUsername(username)
      .setFirstname(firstname != null ? firstname : "")
      .setSurname(surname != null ? surname : "")
      .setFullname(fullname != null ? fullname : "")
      .setCurrency(guru.qa.niffler.grpc.CurrencyValues.valueOf(currency().name()))
      .setPhoto(photoSmall != null ? copyFrom(photoSmall().getBytes()) : copyFrom(new byte[0]))
      .setFriendshipStatus(friendshipStatus() == null ?
        guru.qa.niffler.grpc.FriendshipStatus.VOID :
        guru.qa.niffler.grpc.FriendshipStatus.valueOf(friendshipStatus().name()))
      .build();
  }

  public static @Nonnull UserJson fromJaxb(@Nonnull User jaxbUser) {
    return new UserJson(
        jaxbUser.getId() != null ? UUID.fromString(jaxbUser.getId()) : null,
        jaxbUser.getUsername(),
        jaxbUser.getFirstname(),
        jaxbUser.getSurname(),
        jaxbUser.getFullname(),
        jaxbUser.getCurrency() != null ? CurrencyValues.valueOf(jaxbUser.getCurrency().name()) : null,
        jaxbUser.getPhoto(),
        jaxbUser.getPhotoSmall(),
        (jaxbUser.getFriendshipStatus() != null && jaxbUser.getFriendshipStatus() != jaxb.userdata.FriendshipStatus.VOID)
            ? FriendshipStatus.valueOf(jaxbUser.getFriendshipStatus().name())
            : null
    );
  }

  public static @Nonnull UserJson fromEntity(@Nonnull UserEntity entity, @Nullable FriendshipStatus friendshipStatus) {
    return new UserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getFullname(),
        entity.getCurrency(),
        entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
        entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
        friendshipStatus
    );
  }

  public static @Nonnull UserJson fromEntity(@Nonnull UserEntity entity) {
    return fromEntity(entity, null);
  }
}
