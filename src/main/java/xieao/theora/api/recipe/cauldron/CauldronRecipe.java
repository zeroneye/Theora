package xieao.theora.api.recipe.cauldron;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xieao.theora.api.liquid.Liquid;

import java.util.Arrays;
import java.util.List;

public class CauldronRecipe implements ICauldronRecipe {

    private final Liquid liquid;
    private final List<Object> inputs;

    public CauldronRecipe(Liquid liquid, Object... inputs) {
        this.liquid = liquid;
        this.inputs = Arrays.asList(inputs);
    }

    @Override
    public boolean matches(IInventory inventory, World world, BlockPos pos) {
        List<Object> objects = Lists.newArrayList(inputs);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                boolean flag = false;
                for (Object object : objects) {
                    if (object instanceof Item) {
                        ItemStack stack1 = new ItemStack((Item) object);
                        if (stack.isItemEqual(stack1)) {
                            objects.remove(object);
                            flag = true;
                            break;
                        }
                    } else if (object instanceof Block) {
                        ItemStack stack1 = new ItemStack((Block) object);
                        if (stack.isItemEqual(stack1)) {
                            objects.remove(object);
                            flag = true;
                            break;
                        }
                    } else if (object instanceof ItemStack) {
                        ItemStack stack1 = (ItemStack) object;
                        if (stack.isItemEqual(stack1)) {
                            objects.remove(object);
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    return false;
                }

            }
        }
        return objects.isEmpty();
    }

    @Override
    public List<Object> inputs() {
        return inputs;
    }

    @Override
    public Liquid getLiquid() {
        return liquid;
    }
}
