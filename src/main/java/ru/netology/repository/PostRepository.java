package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final AtomicLong postId = new AtomicLong();
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        if (!posts.isEmpty()) {
            return new ArrayList<>(posts.values());
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long id = postId.getAndIncrement();
            post.setId(id);
            posts.put(id, post);
        } else {
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), post);
            } else {
                long id = postId.get();
                post.setId(id);
                posts.put(id, post);
            }

        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
