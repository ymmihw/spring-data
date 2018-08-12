package com.ymmihw.spring.data.jpa.with.spring.security.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.ymmihw.spring.data.jpa.with.spring.security.models.Tweet;

public interface TweetRepository extends PagingAndSortingRepository<Tweet, Long> {

  @Query("select twt from Tweet twt  JOIN twt.likes as lk where lk = ?#{ principal?.username } or twt.owner = ?#{ principal?.username }")
  Page<Tweet> getMyTweetsAndTheOnesILiked(Pageable pageable);
}
