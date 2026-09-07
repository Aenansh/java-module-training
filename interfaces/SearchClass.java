package interfaces;

import java.util.ArrayList;
import java.util.List;

class ElasticSearch implements SearchEngine {
  @Override
  public List<String> searchResults(String query) {
    System.out.println("Following are the elastic search results matching with the query:");
    return new ArrayList<String>();
  }

  public boolean indexDocument(String id, Object document) {
    String indexName = document.getClass().getSimpleName().toLowerCase();
    System.out.printf("[Elasticsearch] PUT %s/%s/_doc/%s payload: %s\n", indexName, id, id, document);
    return true;
  }
}

class PostgresFullTextSearch implements SearchEngine {
  private final String connectionUrl;

  public PostgresFullTextSearch(String connectionUrl) {
    this.connectionUrl = connectionUrl;
    System.out.println("Connected to postgres on url: " + this.connectionUrl);
  }

  @Override
  public List<String> searchResults(String query) {
    System.out.println("[PostgreSQL] Full-text search for query: " + query);

    return new ArrayList<String>();
  }

  @Override
  public boolean indexDocument(String id, Object document) {
    System.out.printf("[PostgreSQL] INSERT INTO %s_search_idx (id, document) VALUES ('%s', jsonb)\n",
        document.getClass().getSimpleName().toLowerCase(), id);
    return true;
  }
}

class SearchDocument {
  private final String title;
  private final String author;

  SearchDocument(String title, String author) {
    this.title = title;
    this.author = author;
  }

  @Override
  public String toString() {
    return "{title='" + title + "', author='" + author + "'}";
  }
}

public class SearchClass {
  public static void main(String[] args) {
    SearchEngine s1 = new ElasticSearch();
    s1.searchResults("Who invented telescope?");
    SearchDocument document = new SearchDocument("Who invented telescope?", "Hans Lippershey");
    s1.indexDocument("1", document);

    SearchEngine s2 = new PostgresFullTextSearch("postgres://connection.pool.org");
    s2.searchResults("Who invented telescope?");
    s2.indexDocument("1", document);
  }
}
