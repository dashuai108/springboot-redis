package com.example.clund.myredis.repository;

import com.example.clund.myredis.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "user")
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("  from  User p where p.name=:name  ")
    User getUserByName(@Param("name") String name);

    @Cacheable(key = "#p0")
    @Override
    Optional<User> findById(Long id);

    @CachePut(key = "#p0.id")
    @Override
    User save(User user);

    @Modifying
    @Transactional
    @CacheEvict(key = "#p0")
    void deleteById(Long id);

}
