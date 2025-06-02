package guru.qa.niffler.service;

import guru.qa.niffler.grpc.NifflerUserdataServiceGrpc;
import guru.qa.niffler.grpc.UserPageRequest;
import guru.qa.niffler.grpc.UserPageResponse;
import guru.qa.niffler.model.UserJsonBulk;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@GrpcService
public class GrpcUserdataService extends NifflerUserdataServiceGrpc.NifflerUserdataServiceImplBase {

  private final UserService userService;

  @Autowired
  public GrpcUserdataService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void getUsers(UserPageRequest request, StreamObserver<UserPageResponse> responseObserver) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    String searchQuery = request.getSearchQuery().isEmpty() ? null : request.getSearchQuery();
    Page<UserJsonBulk> users = userService.allUsers(request.getUsername(), pageable, searchQuery);

    UserPageResponse response = enrichUsersResponse(users);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getFriends(UserPageRequest request, StreamObserver<UserPageResponse> responseObserver) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    String searchQuery = request.getSearchQuery().isEmpty() ? null : request.getSearchQuery();
    Page<UserJsonBulk> friends = userService.friends(request.getUsername(), pageable, searchQuery);

    UserPageResponse response = enrichUsersResponse(friends);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private UserPageResponse enrichUsersResponse(Page<UserJsonBulk> users) {
    UserPageResponse.Builder responseBuilder = UserPageResponse.newBuilder()
        .setTotalElements((int) users.getTotalElements())
        .setTotalPages(users.getTotalPages())
        .setFirst(users.isFirst())
        .setLast(users.isLast())
        .setSize(users.getSize());

    responseBuilder.addAllEdges(users.stream().map(UserJsonBulk::toGrpcUserResponse).toList());

    return responseBuilder.build();
  }
}
