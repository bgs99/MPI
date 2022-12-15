package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.util.annotation.JsonLocalDateTime;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsResponse {
    private String name;
    private String content;
    @JsonLocalDateTime
    private LocalDateTime dateTime;

    public NewsResponse(News news) {
        this.name = news.getName();
        this.content = news.getContent();
        this.dateTime = news.getDateTime();
    }
}
