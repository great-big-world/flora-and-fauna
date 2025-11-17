package dev.creoii.greatbigworld.floraandfauna.season;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TranslatableOption;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public enum TransitionQuality implements TranslatableOption, StringIdentifiable {
    HIGH(36, "options.seasonTransitionQuality.high"),
    NORMAL(22, "options.seasonTransitionQuality.normal"),
    LOW(8, "options.seasonTransitionQuality.low"),
    INSTANT(1, "options.seasonTransitionQuality.instant");

    public static final Codec<TransitionQuality> CODEC = StringIdentifiable.createCodec(TransitionQuality::values);
    public static final Text[] NAMES = Arrays.stream(values()).map(TranslatableOption::getText).toArray(Text[]::new);
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
        public static final CustomPayload.Id<SyncTransitionQuality> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "sync_season_transition_quality"));
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
