package com.samschabel.pw.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samschabel.pw.api.model.blog.Article;
import com.samschabel.pw.api.service.security.blog.BlogService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public List<Article> getAllArticles() {
        return blogService.getAllArticles();
    }

    @PostMapping("/article/create")
    public Article createArticle(Article article) {
        return blogService.createArticle(article);
    }

    @PostMapping("/article/update")
    public Article updateArticle(Article article) {
        return blogService.updateArticle(article);
    }

    @PostMapping("/article/delete")
    public Article deleteArticle(Article article) {
        return blogService.deleteArticle(article);
    }

}