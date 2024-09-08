package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.world.decorator.AlterGroundTreeDecorator;
import dev.creoii.greatbigworld.floraandfauna.world.decorator.BranchTreeDecorator;
import dev.creoii.greatbigworld.floraandfauna.world.decorator.MossTreeDecorator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public final class FloraAndFaunaTreeDecoratorTypes {
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<MossTreeDecorator> MOSS = new TreeDecoratorType<>(MossTreeDecorator.CODEC);
    public static final TreeDecoratorType<AlterGroundTreeDecorator> ALTER_GROUND = new TreeDecoratorType<>(AlterGroundTreeDecorator.CODEC);

    public static void register() {
        Registry.register(Registries.TREE_DECORATOR_TYPE, new Identifier(FloraAndFauna.NAMESPACE, "branch"), BRANCH);
        Registry.register(Registries.TREE_DECORATOR_TYPE, new Identifier(FloraAndFauna.NAMESPACE, "moss"), MOSS);
        Registry.register(Registries.TREE_DECORATOR_TYPE, new Identifier(FloraAndFauna.NAMESPACE, "alter_ground"), ALTER_GROUND);
    }
}
