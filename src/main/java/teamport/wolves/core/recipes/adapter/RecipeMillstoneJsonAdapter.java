package teamport.wolves.core.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import teamport.wolves.core.recipes.entry.RecipeEntryMillstone;

import java.lang.reflect.Type;

public class RecipeMillstoneJsonAdapter implements RecipeJsonAdapter<RecipeEntryMillstone> {
	@Override
	public RecipeEntryMillstone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		RecipeSymbol input = context.deserialize(obj.get("input").getAsJsonObject(), RecipeSymbol.class);
		ItemStack output = context.deserialize(obj.get("output").getAsJsonObject(), ItemStack.class);

		return new RecipeEntryMillstone(input, output);
	}

	@Override
	public JsonElement serialize(RecipeEntryMillstone src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();

		obj.addProperty("name", src.toString());
		obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
		obj.add("input", context.serialize(src.getInput(), RecipeSymbol.class));
		obj.add("output", context.serialize(src.getOutput(), ItemStack.class));

		return obj;
	}
}
