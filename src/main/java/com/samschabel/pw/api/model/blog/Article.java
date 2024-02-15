package com.samschabel.pw.api.model.blog;

import java.sql.Blob;
import java.sql.Clob;
import java.util.List;

import jakarta.validation.Valid;
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
    private String content;
    private List<@Valid @NotBlank String> tags;

}
