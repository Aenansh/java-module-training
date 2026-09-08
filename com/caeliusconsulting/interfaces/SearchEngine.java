package com.caeliusconsulting.interfaces;

import java.util.List;

public interface SearchEngine {
  public List<String> searchResults(String query);

  boolean indexDocument(String id, Object document);
}
