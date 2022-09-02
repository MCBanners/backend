package com.mcbanners.bannerapi.service.impl.resource.backend;

import com.mcbanners.bannerapi.net.PolymartClient;
import com.mcbanners.bannerapi.obj.backend.polymart.PolymartResource;
import com.mcbanners.bannerapi.obj.generic.PriceInformation;
import com.mcbanners.bannerapi.obj.generic.RatingInformation;
import com.mcbanners.bannerapi.obj.generic.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PolymartResourceService {
    private final PolymartClient client;

    @Autowired
    public PolymartResourceService(PolymartClient client) {
        this.client = client;
    }

    public Resource handlePolymart(final int resourceId) {
        final PolymartResource resource = loadPolymartResource(resourceId);
        if (resource == null) {
            return null;
        }

        final String image = client.getBase64Image(resource.thumbnailURL());
        final boolean isPremium = !(resource.price() == 0.00);

        return new Resource(
                image,
                resource.title(),
                resource.ownerId(),
                resource.ownerName(),
                new RatingInformation(
                        resource.reviewCount(),
                        (double) resource.stars()
                ),
                resource.downloads(),
                isPremium ? new PriceInformation(resource.price(), resource.currency().toUpperCase(Locale.ROOT)) : null,
                null
        );
    }

    private PolymartResource loadPolymartResource(final int resourceId) {
        final ResponseEntity<PolymartResource> resp = client.getResource(resourceId);
        return resp == null ? null : resp.getBody();
    }
}
