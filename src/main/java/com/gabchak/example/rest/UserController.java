package com.gabchak.example.rest;

import com.gabchak.example.dto.SubscriptionDto;
import com.gabchak.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping(UserController.CRUD_PATH)
public class UserController {
  public static final String CRUD_PATH = "/users";
  public static final String SUBSCRIBE = "/subscribe";
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  /**
   * The method update subscription date
   * if it is not exist of expired.
   *
   * You can get user data from Principal.
   *
   * @param principal user params from spring security
   * @return {@link SubscriptionDto}
   * */
  @PutMapping(SUBSCRIBE)
  public ResponseEntity<SubscriptionDto> subscribe(Principal principal) {
    LocalDate paidBefore = userService.subscribe(principal.getName());
    return ResponseEntity.ok(new SubscriptionDto(paidBefore));
  }

  /**
   * The method returns subscription date
   * if the date exists.
   *
   * You can get user data from Principal.
   *
   * @param principal user params from spring security
   * @return {@link SubscriptionDto}
   * */
  @GetMapping(SUBSCRIBE)
  public ResponseEntity findSubscription(Principal principal) {
    return userService.findByEmail(principal.getName())
        .map(user -> {
          LocalDate subscription = user.getSubscription();
          if (subscription != null) {
            return ResponseEntity.ok(
                new SubscriptionDto(user.getSubscription()));
          }
          return ResponseEntity.noContent().build();
        })
        .orElseGet(ResponseEntity.noContent()::build);
  }
}
