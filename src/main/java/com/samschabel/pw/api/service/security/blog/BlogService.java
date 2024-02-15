package com.samschabel.pw.api.service.security.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.samschabel.pw.api.entity.blog.ArticleEntity;
import com.samschabel.pw.api.exception.DataNotFoundException;
import com.samschabel.pw.api.model.blog.Article;
import com.samschabel.pw.api.repository.blog.ArticleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogService {

    private final ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        List<ArticleEntity> entities = articleRepository.findAll();
        entities.forEach(entity -> articles.add(entity.toModel()));
        return articles;
    }

    public Article createArticle(Article article) {
        ArticleEntity entity = new ArticleEntity();
        entity.setContent(article.getContent());
        entity.setTags(article.getTags());
        entity.setTitle(article.getTitle());
        entity = articleRepository.save(entity);
        article.setId(entity.getId());
        return article;
    }

    public Article updateArticle(Article article) {
        Optional<ArticleEntity> optional = articleRepository.findById(article.getId());
        if(optional.isPresent()) {
            ArticleEntity entity = optional.get();
            entity.setContent(article.getContent());
            entity.setTags(article.getTags());
            entity.setTitle(article.getTitle());
            articleRepository.save(entity);
            return article;
        } else {
            throw new DataNotFoundException("Article not found.");
        }
    }

    public Article deleteArticle(Article article) {
        Optional<ArticleEntity> optional = articleRepository.findById(article.getId());
        if(optional.isPresent()) {
            ArticleEntity entity = optional.get();
            articleRepository.delete(entity);
            return article;
        } else {
            throw new DataNotFoundException("Article not found.");
        }
    }

}
