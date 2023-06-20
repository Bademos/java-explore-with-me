package ru.practicum.server.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ViewStat {
    String app;
    String uri;
    Long hit;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewStats {
        private String app;
        private String uri;
        private Long hits;
    }
}
