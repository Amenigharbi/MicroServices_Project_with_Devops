package com.devops.commentsservice.repository;

import com.devops.commentsservice.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {
List<Comments> findByattractionId(long id);
    List<Comments> findByTouristId(long id);



}
