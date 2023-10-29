package kr.apartribebackend.article.dto.announce;

import kr.apartribebackend.article.domain.Announce;

public record AnnounceWidgetRes(
        Long id,
        String level,
        String title
) {
    public static AnnounceWidgetRes from(Announce announce) {
        return new AnnounceWidgetRes(
                announce.getId(), announce.getLevel().getName(), announce.getTitle()
        );
    }

}
