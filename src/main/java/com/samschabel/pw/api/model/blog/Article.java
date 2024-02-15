package com.samschabel.pw.api.model.blog;

import java.sql.Blob;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private Blob content;
    private List<String> tags;

}
