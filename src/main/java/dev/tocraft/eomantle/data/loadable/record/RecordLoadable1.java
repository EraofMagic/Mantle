package dev.tocraft.eomantle.data.loadable.record;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import dev.tocraft.eomantle.data.loadable.field.RecordField;
import dev.tocraft.eomantle.util.typed.TypedMap;

import java.util.function.Function;

/** Record loadable with a single field */
record RecordLoadable1<A,R>(
  RecordField<A,? super R> fieldA,
  Function<A,R> constructor
) implements RecordLoadable<R> {
  @Override
  public R deserialize(JsonObject json, TypedMap context) {
    return constructor.apply(fieldA.get(json, context));
  }

  @Override
  public void serialize(R object, JsonObject json) {
    fieldA.serialize(object, json);
  }

  @Override
  public R decode(FriendlyByteBuf buffer, TypedMap context) {
    return constructor.apply(fieldA.decode(buffer, context));
  }

  @Override
  public void encode(FriendlyByteBuf buffer, R object) {
    fieldA.encode(buffer, object);
  }
}
