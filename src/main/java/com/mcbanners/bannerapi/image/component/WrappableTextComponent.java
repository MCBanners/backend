package com.mcbanners.bannerapi.image.component;

import com.mcbanners.bannerapi.banner.BannerOutputType;
import com.mcbanners.bannerapi.image.ImageBuilder;
import com.mcbanners.bannerapi.util.StringUtil;

public class WrappableTextComponent extends TextComponent {
    private final int maxLength;

    public WrappableTextComponent(TextComponent component) {
        this(component, 9999);
    }

    public WrappableTextComponent(TextComponent component, int maxLength) {
        super(
                component.x,
                component.y,
                component.fontSize,
                component.fontColor,
                component.bold,
                component.textAlign,
                component.font,
                component.content
        );

        this.maxLength = maxLength;
    }

    @Override
    public ImageBuilder draw(ImageBuilder builder, BannerOutputType outputType) {
        return build(builder)
                .content(StringUtil.truncateAfter(this.content, this.maxLength), this.font)
                .wrap(this.fontSize, 295 - this.x)
                .finishText();
    }
}
