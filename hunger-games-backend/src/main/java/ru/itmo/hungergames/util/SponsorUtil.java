package ru.itmo.hungergames.util;

import org.springframework.stereotype.Component;
import ru.itmo.hungergames.exception.NotNewsSubscribedException;
import ru.itmo.hungergames.model.entity.user.Sponsor;

@Component
public class SponsorUtil {
    public void checkIfSponsorCanSeeNews(Sponsor sponsor) {
        if (sponsor.getNewsSubscriptionOrder() == null || !sponsor.getNewsSubscriptionOrder().isPaid()) {
            throw new NotNewsSubscribedException();
        }
    }
}
