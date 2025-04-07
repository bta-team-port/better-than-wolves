package teamport.wolves.core.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import teamport.wolves.core.recipes.entry.RecipeEntryCauldron;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeCauldronJsonAdapter implements RecipeJsonAdapter<RecipeEntryCauldron> {
	@Override
	public RecipeEntryCauldron deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		ItemStack output = context.deserialize(obj.get("output").getAsJsonObject(), ItemStack.class);

		List<JsonElement> jsonElements = obj.get("inputs").getAsJsonArray().asList();
		List<RecipeSymbol> inputs = new ArrayList<>();

		for (JsonElement element : jsonElements) {
			RecipeSymbol input = context.deserialize(element, RecipeSymbol.class);
			inputs.add(input);
		}

		return new RecipeEntryCauldron(inputs, output);
	}

	@Override
	public JsonElement serialize(RecipeEntryCauldron src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();

		obj.addProperty("name", src.toString());
		obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
		obj.add("input", context.serialize(src.getInput(), RecipeSymbol.class));
		obj.add("output", context.serialize(src.getOutput(), ItemStack.class));

		return obj;
	}
}
