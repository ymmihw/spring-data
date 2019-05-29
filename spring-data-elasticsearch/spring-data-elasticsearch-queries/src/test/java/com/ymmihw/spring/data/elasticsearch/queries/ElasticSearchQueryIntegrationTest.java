package com.ymmihw.spring.data.elasticsearch.queries;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.junit.Assert.assertEquals;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.elasticsearch.queries.ElasticSearchQueryIntegrationTest.DockerConfig;
import com.ymmihw.spring.data.elasticsearch.queries.model.Article;
import com.ymmihw.spring.data.elasticsearch.queries.model.Author;
import com.ymmihw.spring.data.elasticsearch.queries.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DockerConfig.class)
public class ElasticSearchQueryIntegrationTest {

  @ClassRule
  public static MyElasticsearchContainer container = MyElasticsearchContainer.getInstance();

  @Configuration
  @EnableElasticsearchRepositories(
      basePackages = "com.ymmihw.spring.data.elasticsearch.queries.repository")
  @ComponentScan(basePackages = {"com.ymmihw.spring.data.elasticsearch.queries.service"})
  public static class DockerConfig {

    @Bean
    public Client client() {
      Settings settings = Settings.builder().put("client.transport.sniff", false).build();
      PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
      try {
        TransportClient addTransportAddress = preBuiltTransportClient.addTransportAddress(
            new TransportAddress(InetAddress.getByName(container.getContainerIpAddress()),
                container.getMappedPort(9300)));
        return addTransportAddress;
      } catch (final UnknownHostException ioex) {
        throw new RuntimeException(ioex);
      }

    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
      return new ElasticsearchTemplate(client());
    }
  }

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private Client client;

  private final Author johnSmith = new Author("John Smith");
  private final Author johnDoe = new Author("John Doe");

  @Before
  public void before() {
    elasticsearchTemplate.deleteIndex(Article.class);
    elasticsearchTemplate.createIndex(Article.class);
    elasticsearchTemplate.putMapping(Article.class);
    elasticsearchTemplate.refresh(Article.class);

    Article article = new Article("Spring Data Elasticsearch");
    article.setAuthors(asList(johnSmith, johnDoe));
    article.setTags("elasticsearch", "spring data");
    articleService.save(article);

    article = new Article("Search engines");
    article.setAuthors(asList(johnDoe));
    article.setTags("search engines", "tutorial");
    articleService.save(article);

    article = new Article("Second Article About Elasticsearch");
    article.setAuthors(asList(johnSmith));
    article.setTags("elasticsearch", "spring data");
    articleService.save(article);

    article = new Article("Elasticsearch Tutorial");
    article.setAuthors(asList(johnDoe));
    article.setTags("elasticsearch");
    articleService.save(article);
  }

  @Test
  public void givenFullTitle_whenRunMatchQuery_thenDocIsFound() {
    final SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title", "Search engines").operator(AND)).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());
  }

  @Test
  public void givenOneTermFromTitle_whenRunMatchQuery_thenDocIsFound() {
    final SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(matchQuery("title", "Engines Solutions")).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());
    assertEquals("Search engines", articles.get(0).getTitle());
  }

  @Test
  public void givenPartTitle_whenRunMatchQuery_thenDocIsFound() {
    final SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(matchQuery("title", "elasticsearch data")).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(3, articles.size());
  }

  @Test
  public void givenFullTitle_whenRunMatchQueryOnVerbatimField_thenDocIsFound() {
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title.verbatim", "Second Article About Elasticsearch")).build();
    List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());

    searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title.verbatim", "Second Article About")).build();
    articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(0, articles.size());
  }

  @Test
  public void givenNestedObject_whenQueryByAuthorsName_thenFoundArticlesByThatAuthor() {
    final QueryBuilder builder =
        nestedQuery("authors", boolQuery().must(termQuery("authors.name", "smith")), ScoreMode.Avg);

    final SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);

    assertEquals(2, articles.size());
  }

  @Test
  public void givenAnalyzedQuery_whenMakeAggregationOnTermCount_thenEachTokenCountsSeparately() {
    final TermsAggregationBuilder aggregation =
        AggregationBuilders.terms("top_tags").field("title.text");
    final SearchResponse response = client.prepareSearch("blog").setTypes("article")
        .addAggregation(aggregation).execute().actionGet();

    final Map<String, Aggregation> results = response.getAggregations().asMap();
    final StringTerms topTags = (StringTerms) results.get("top_tags");

    final List<String> keys =
        topTags.getBuckets().stream().map(b -> b.getKeyAsString()).collect(toList());
    Collections.sort(keys);
    assertEquals(asList("about", "article", "data", "elasticsearch", "engines", "search", "second",
        "spring", "tutorial"), keys);
  }

  @Test
  public void givenNotAnalyzedQuery_whenMakeAggregationOnTermCount_thenEachTermCountsIndividually() {
    final TermsAggregationBuilder aggregation =
        AggregationBuilders.terms("top_tags").field("tags").order(BucketOrder.count(false));
    final SearchResponse response = client.prepareSearch("blog").setTypes("article")
        .addAggregation(aggregation).execute().actionGet();

    final Map<String, Aggregation> results = response.getAggregations().asMap();
    final StringTerms topTags = (StringTerms) results.get("top_tags");

    final List<String> keys =
        topTags.getBuckets().stream().map(b -> b.getKeyAsString()).collect(toList());
    assertEquals(asList("elasticsearch", "spring data", "search engines", "tutorial"), keys);
  }

  @Test
  public void givenNotExactPhrase_whenUseSlop_thenQueryMatches() {
    final SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchPhraseQuery("title", "spring elasticsearch").slop(1)).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());
  }

  @Test
  public void givenPhraseWithType_whenUseFuzziness_thenQueryMatches() {
    final SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(matchQuery("title", "spring date elasticserch")
            .operator(AND).fuzziness(Fuzziness.ONE).prefixLength(3)).build();

    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());
  }

  @Test
  public void givenMultimatchQuery_whenDoSearch_thenAllProvidedFieldsMatch() {
    final SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(multiMatchQuery("tutorial").field("title")
            .field("tags").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)).build();

    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(2, articles.size());
  }
}
