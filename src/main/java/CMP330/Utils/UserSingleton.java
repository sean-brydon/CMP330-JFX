package CMP330.Utils;


import CMP330.model.User;

public final class UserSingleton {
  private User user;

  private final static UserSingleton INSTANCE = new UserSingleton();

  private UserSingleton() {
  }

  public static UserSingleton getInstance() {
    return INSTANCE;
  }

  public void setUser(User u) {
    this.user = u;
  }

  public User getUser() {
    return this.user;
  }
}

