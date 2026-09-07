package interfaces;

class OAuth implements Auth {
  private String userId;
  private String token;

  @Override
  public AuthResponse authenticate(AuthRequest request) {
    System.out.println("User authenticated through OAuth.");
    userId = "Aenansh";
    token = "random-token";
    return null;
  }

  @Override
  public boolean verifyToken(String token) {
    if (this.token == token) {
      System.out.println("OAuth says you have correct token.");
      return true;
    }
    System.out.println("OAuth says you don't possess the correct token.");
    return false;
  }

  @Override
  public void revokeSession(String userId) {
    System.out.println("User signed out by OAuth. UserId: " + this.userId);
    userId = null;
  }
}

class JWTAuth implements Auth {
  private String algorithm;
  private String data;
  private String token;

  @Override
  public AuthResponse authenticate(AuthRequest request) {
    System.out.println("User authenticated through JWT.");
    data = "{id: 1, name: Aenansh}";
    algorithm = "HS256";
    token = "random-token";
    return null;
  }

  @Override
  public boolean verifyToken(String token) {
    System.out.println("Using alogrithm for hashing and verifying. " + algorithm);
    if (this.token == token) {
      System.out.println("JWT says you have correct token.");
      return true;
    }
    System.out.println("JWT says you don't possess the correct token.");
    return false;
  }

  @Override
  public void revokeSession(String data) {
    System.out.println("User signed out by JWT. UserId: " + this.data);
    this.data = null;
  }
}

public class AuthClass {
  public static void main(String[] args) {
    Auth oa = new OAuth();

    oa.authenticate(null);
    oa.verifyToken("random-token");
    oa.revokeSession("Aenansh");

    Auth ja = new JWTAuth();

    ja.authenticate(null);
    ja.verifyToken("random-token");
    ja.revokeSession("Aenansh");
  }
}
