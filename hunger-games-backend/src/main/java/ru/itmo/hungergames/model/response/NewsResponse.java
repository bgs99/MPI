package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.util.annotation.JsonDateTime;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class NewsResponse {
    private String name;
    private String content;
    @JsonDateTime
    private Instant dateTime;

    public NewsResponse(News news) {
        this.name = news.getName();
        this.content = news.getContent();
        this.dateTime = news.getDateTime();
    }
}
