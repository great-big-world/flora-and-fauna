package dev.creoii.greatbigworld.floraandfauna.season;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TranslatableOption;

@Environment(EnvType.CLIENT)
public enum TransitionQuality implements TranslatableOption, StringIdentifiable {
    HIGH(40, "options.seasonTransitionQuality.high"),
    NORMAL(25, "options.seasonTransitionQuality.normal"),
    LOW(10, "options.seasonTransitionQuality.low"),
    INSTANT(1, "options.seasonTransitionQuality.instant");

    public static final Codec<TransitionQuality> CODEC = StringIdentifiable.createCodec(TransitionQuality::values);
    private final int quality;
    private final String translationKey;

    TransitionQuality(int quality, String translationKey) {
        this.quality = quality;
        this.translationKey = translationKey;
    }

    public int getQuality() {
        return quality;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    public String asString() {
        return name().toLowerCase();
    }

    public record SyncTransitionQuality(byte quality) implements CustomPayload {
        public static final CustomPayload.Id<SyncTransitionQuality> PACKET_ID = new CustomPayload.Id<>(new Identifier(FloraAndFauna.NAMESPACE, "sync_season_transition_quality"));
        public static final PacketCodec<RegistryByteBuf, SyncTransitionQuality> PACKET_CODEC = PacketCodec.of(SyncTransitionQuality::write, SyncTransitionQuality::new);

        public SyncTransitionQuality(RegistryByteBuf buf) {
            this(buf.readByte());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeByte(quality);
        }

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
