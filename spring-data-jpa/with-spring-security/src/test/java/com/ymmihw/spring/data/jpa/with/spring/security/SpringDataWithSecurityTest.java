package com.ymmihw.spring.data.jpa.with.spring.security;

import static org.springframework.util.Assert.isTrue;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.ymmihw.spring.data.jpa.with.spring.security.data.repositories.TweetRepository;
import com.ymmihw.spring.data.jpa.with.spring.security.data.repositories.UserRepository;
import com.ymmihw.spring.data.jpa.with.spring.security.models.AppUser;
import com.ymmihw.spring.data.jpa.with.spring.security.models.Tweet;
import com.ymmihw.spring.data.jpa.with.spring.security.security.AppUserPrincipal;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext
public class SpringDataWithSecurityTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TweetRepository tweetRepository;

  @Before
  public void testInit() {
    List<AppUser> appUsers =
        (List<AppUser>) userRepository.saveAll(DummyContentUtil.generateDummyUsers());
    tweetRepository.saveAll(DummyContentUtil.generateDummyTweets(appUsers));
  }

  @After
  public void tearDown() {
    tweetRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void givenAppUser_whenLoginSuccessful_shouldUpdateLastLogin() {
    AppUser appUser = userRepository.findByUsername("lionel@messi.com");
    Authentication auth = new UsernamePasswordAuthenticationToken(new AppUserPrincipal(appUser),
        null, DummyContentUtil.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    userRepository.updateLastLogin(new Date());
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void givenNoAppUserInSecurityContext_whenUpdateLastLoginAttempted_shouldFail() {
    userRepository.updateLastLogin(new Date());
  }

  @Test
  public void givenAppUser_whenLoginSuccessful_shouldReadMyPagedTweets() {
    AppUser appUser = userRepository.findByUsername("lionel@messi.com");
    Authentication auth = new UsernamePasswordAuthenticationToken(new AppUserPrincipal(appUser),
        null, DummyContentUtil.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    Page<Tweet> page = null;
    do {
      page = tweetRepository
          .getMyTweetsAndTheOnesILiked(PageRequest.of(page != null ? page.getNumber() + 1 : 0, 5));
      for (Tweet twt : page.getContent()) {
        isTrue((twt.getOwner() == appUser.getUsername())
            || (twt.getLikes().contains(appUser.getUsername())), "I do not have any Tweets");
      }
    } while (page.hasNext());
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void givenNoAppUser_whenPaginatedResultsRetrievalAttempted_shouldFail() {
    Page<Tweet> page = null;
    do {
      page = tweetRepository
          .getMyTweetsAndTheOnesILiked(PageRequest.of(page != null ? page.getNumber() + 1 : 0, 5));
    } while (page != null && page.hasNext());
  }
}
