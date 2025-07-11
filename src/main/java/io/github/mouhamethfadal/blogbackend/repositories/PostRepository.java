package io.github.mouhamethfadal.blogbackend.repositories;

import io.github.mouhamethfadal.blogbackend.entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
