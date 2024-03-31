package slimeknights.tconstruct.tools.recipe;

import com.mojang.datafixers.util.Function5;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.json.predicate.modifier.ModifierPredicate;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.worktable.AbstractSizedIngredientRecipeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** Builder for {@link ModifierRemovalRecipe} and {@link ExtractModifierRecipe} */
@RequiredArgsConstructor(staticName = "removal")
public class ModifierRemovalRecipeBuilder extends AbstractSizedIngredientRecipeBuilder<ModifierRemovalRecipeBuilder> {
  private final Function5<ResourceLocation,SizedIngredient,List<SizedIngredient>,List<ItemStack>,IJsonPredicate<ModifierId>,ModifierRemovalRecipe> constructor;
  private final List<ItemStack> leftovers = new ArrayList<>();
  private SizedIngredient tools = ModifierRemovalRecipe.DEFAULT_TOOLS;
  @Setter
  @Accessors(fluent = true)
  private IJsonPredicate<ModifierId> modifierPredicate = ModifierPredicate.ANY;

  public static ModifierRemovalRecipeBuilder removal() {
    return removal(ModifierRemovalRecipe::new);
  }

  public static ModifierRemovalRecipeBuilder extract() {
    return removal(ExtractModifierRecipe::new);
  }

  /**
   * Sets the tool requirement for this recipe
   */
  public ModifierRemovalRecipeBuilder setTools(SizedIngredient ingredient) {
    this.tools = ingredient;
    return this;
  }

  /**
   * Sets the tool requirement for this recipe
   */
  public ModifierRemovalRecipeBuilder setTools(Ingredient ingredient) {
    return setTools(SizedIngredient.of(ingredient));
  }

  /**
   * Adds a leftover stack to the recipe
   */
  public ModifierRemovalRecipeBuilder addLeftover(ItemStack stack) {
    leftovers.add(stack);
    return this;
  }

  /**
   * Adds a leftover stack to the recipe
   */
  public ModifierRemovalRecipeBuilder addLeftover(ItemLike item) {
    return addLeftover(new ItemStack(item));
  }

  @Override
  public void save(Consumer<FinishedRecipe> consumer) {
    save(consumer, Registry.ITEM.getKey(leftovers.get(0).getItem()));
  }

  @Override
  public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
    if (inputs.isEmpty()) {
      throw new IllegalStateException("Must have at least one input");
    }
    ResourceLocation advancementId = buildOptionalAdvancement(id, "modifiers");
    consumer.accept(new LoadableFinishedRecipe<>(constructor.apply(id, tools, inputs, leftovers, modifierPredicate), ModifierRemovalRecipe.LOADER, advancementId));
  }
}