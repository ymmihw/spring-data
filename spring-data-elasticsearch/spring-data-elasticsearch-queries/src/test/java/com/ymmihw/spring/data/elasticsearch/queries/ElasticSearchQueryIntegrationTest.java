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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.elasticsearch.MyElasticsearchContainer;
import com.ymmihw.spring.data.elasticsearch.queries.ElasticSearchQueryIntegrationTest.DockerClient;
import com.ymmihw.spring.data.elasticsearch.queries.config.Config;
import com.ymmihw.spring.data.elasticsearch.queries.model.Article;
import com.ymmihw.spring.data.elasticsearch.queries.model.Author;
import com.ymmihw.spring.data.elasticsearch.queries.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class, DockerClient.class})
public class ElasticSearchQueryIntegrationTest {

  @ClassRule
  public static MyElasticsearchContainer container = MyElasticsearchContainer.getInstance();

  @Configuration
  public static class DockerClient extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
      final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
          .connectedTo(container.getContainerIpAddress() + ":" + container.getMappedPort(9200))
          .build();

      return RestClients.create(clientConfiguration).rest();
    }

  }

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  @Autowired
  private ArticleService articleService;

  private final Author johnSmith = new Author("John Smith");
  private final Author johnDoe = new Author("John Doe");

  @Before
  public void before() {
    IndexOperations indexOperations = elasticsearchOperations.indexOps(Article.class);
    indexOperations.delete();
    indexOperations.create();
    indexOperations.putMapping();
    indexOperations.refresh();

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
    final Query searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title", "Search engines").operator(AND)).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();
    assertEquals(1, articles.size());
  }

  @Test
  public void givenOneTermFromTitle_whenRunMatchQuery_thenDocIsFound() {
    final Query searchQuery =
        new NativeSearchQueryBuilder().withQuery(matchQuery("title", "Engines Solutions")).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();
    assertEquals(1, articles.size());
    assertEquals("Search engines", articles.get(0).getContent().getTitle());
  }

  @Test
  public void givenPartTitle_whenRunMatchQuery_thenDocIsFound() {
    final Query searchQuery =
        new NativeSearchQueryBuilder().withQuery(matchQuery("title", "elasticsearch data")).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;
    assertEquals(3, articles.size());
  }

  @Test
  public void givenFullTitle_whenRunMatchQueryOnVerbatimField_thenDocIsFound() {
    Query searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title.verbatim", "Second Article About Elasticsearch")).build();
    List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;
    assertEquals(1, articles.size());

    searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title.verbatim", "Second Article About")).build();
    articles = elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;
    assertEquals(0, articles.size());
  }

  @Test
  public void givenNestedObject_whenQueryByAuthorsName_thenFoundArticlesByThatAuthor() {
    final QueryBuilder builder =
        nestedQuery("authors", boolQuery().must(termQuery("authors.name", "smith")), ScoreMode.Avg);

    final Query searchQuery = new NativeSearchQueryBuilder().withQuery(builder).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;

    assertEquals(2, articles.size());
  }

  @Test
  public void givenAnalyzedQuery_whenMakeAggregationOnTermCount_thenEachTokenCountsSeparately() {
    final TermsAggregationBuilder aggregation =
        AggregationBuilders.terms("top_tags").field("title.text");
    Query query = new NativeSearchQueryBuilder().addAggregation(aggregation).build();
    SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
    Aggregations aggregations = searchHits.getAggregations();
    final Map<String, Aggregation> results = aggregations.asMap();
    final ParsedStringTerms topTags = (ParsedStringTerms) results.get("top_tags");

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
    Query query = new NativeSearchQueryBuilder().addAggregation(aggregation).build();
    SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
    Aggregations aggregations = searchHits.getAggregations();
    final Map<String, Aggregation> results = aggregations.asMap();
    final ParsedStringTerms topTags = (ParsedStringTerms) results.get("top_tags");

    final List<String> keys =
        topTags.getBuckets().stream().map(b -> b.getKeyAsString()).collect(toList());
    assertEquals(asList("elasticsearch", "spring data", "search engines", "tutorial"), keys);
  }

  @Test
  public void givenNotExactPhrase_whenUseSlop_thenQueryMatches() {
    final Query searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchPhraseQuery("title", "spring elasticsearch").slop(1)).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;
    assertEquals(1, articles.size());
  }

  @Test
  public void givenPhraseWithType_whenUseFuzziness_thenQueryMatches() {
    final Query searchQuery =
        new NativeSearchQueryBuilder().withQuery(matchQuery("title", "spring date elasticserch")
            .operator(AND).fuzziness(Fuzziness.ONE).prefixLength(3)).build();

    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;
    assertEquals(1, articles.size());
  }

  @Test
  public void givenMultimatchQuery_whenDoSearch_thenAllProvidedFieldsMatch() {
    final Query searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery("tutorial")
        .field("title").field("tags").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)).build();

    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();;
    assertEquals(2, articles.size());
  }
}
