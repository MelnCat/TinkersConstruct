package slimeknights.tconstruct.smeltery.client.screen;

import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.renderer.Rect2i;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Interface for JEI support to determine the ingredient under the mouse
 */
public interface IScreenWithFluidTank {
  /**
   * Gets the ingredient under the mouse, typically a fluid
   * @param mouseX Mouse X
   * @param mouseY Mouse Y
   * @return Ingredient under mouse, or null if no ingredient. Does not need to handle item stacks
   */
  Pair<?, Rect2i> getClickableIngredientUnderMouse(double mouseX, double mouseY);
}
