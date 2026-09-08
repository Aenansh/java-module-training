package com.caeliusconsulting.interfaces;

class AuthRequest {
}

class AuthResponse {
}

public interface Auth {
  AuthResponse authenticate(AuthRequest request);

  boolean verifyToken(String token);

  void revokeSession(String userId);
}
