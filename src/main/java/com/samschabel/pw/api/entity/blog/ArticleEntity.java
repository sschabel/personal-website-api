package com.samschabel.pw.api.entity.blog;

import java.sql.Clob;
import java.util.List;

import com.samschabel.pw.api.entity.StringListConverter;
import com.samschabel.pw.api.model.blog.Article;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ARTICLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Lob
    private String content;
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    public Article toModel() {
        return new Article(this.id, this.title, this.content, this.tags);
    }
}
