package dev.creoii.greatbigworld.floraandfauna.season;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import java.util.Arrays;

@Environment(EnvType.CLIENT)
public enum TransitionQuality implements StringRepresentable {
    HIGH(36, "options.seasonTransitionQuality.high"),
    NORMAL(22, "options.seasonTransitionQuality.normal"),
    LOW(8, "options.seasonTransitionQuality.low"),
    INSTANT(1, "options.seasonTransitionQuality.instant");

    public static final Codec<TransitionQuality> CODEC = StringRepresentable.fromEnum(TransitionQuality::values);
    public static final Component[] NAMES = Arrays.stream(values()).map(transitionQuality -> Component.translatable(transitionQuality.translationKey)).toArray(Component[]::new);
    private final int quality;
    private final String translationKey;

    TransitionQuality(int quality, String translationKey) {
        this.quality = quality;
        this.translationKey = translationKey;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public record SyncTransitionQuality(byte quality) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SyncTransitionQuality> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sync_season_transition_quality"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncTransitionQuality> PACKET_CODEC = StreamCodec.ofMember(SyncTransitionQuality::write, SyncTransitionQuality::new);

        public SyncTransitionQuality(RegistryFriendlyByteBuf buf) {
            this(buf.readByte());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeByte(quality);
        }

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
