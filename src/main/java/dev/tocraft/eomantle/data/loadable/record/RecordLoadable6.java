package dev.tocraft.eomantle.data.loadable.record;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function6;
import net.minecraft.network.FriendlyByteBuf;
import dev.tocraft.eomantle.data.loadable.field.RecordField;
import dev.tocraft.eomantle.util.typed.TypedMap;

/** Record loadable with 6 fields */
@SuppressWarnings("DuplicatedCode")
record RecordLoadable6<A,B,C,D,E,F,G,R>(
  RecordField<A,? super R> fieldA,
  RecordField<B,? super R> fieldB,
  RecordField<C,? super R> fieldC,
  RecordField<D,? super R> fieldD,
  RecordField<E,? super R> fieldE,
  RecordField<F,? super R> fieldF,
  Function6<A,B,C,D,E,F,R> constructor
) implements RecordLoadable<R> {
  @Override
  public R deserialize(JsonObject json, TypedMap context) {
    return constructor.apply(
      fieldA.get(json, context),
      fieldB.get(json, context),
      fieldC.get(json, context),
      fieldD.get(json, context),
      fieldE.get(json, context),
      fieldF.get(json, context)
    );
  }

  @Override
  public void serialize(R object, JsonObject json) {
    fieldA.serialize(object, json);
    fieldB.serialize(object, json);
    fieldC.serialize(object, json);
    fieldD.serialize(object, json);
    fieldE.serialize(object, json);
    fieldF.serialize(object, json);
  }

  @Override
  public R decode(FriendlyByteBuf buffer, TypedMap context) {
    return constructor.apply(
      fieldA.decode(buffer, context),
      fieldB.decode(buffer, context),
      fieldC.decode(buffer, context),
      fieldD.decode(buffer, context),
      fieldE.decode(buffer, context),
      fieldF.decode(buffer, context)
    );
  }

  @Override
  public void encode(FriendlyByteBuf buffer, R object) {
    fieldA.encode(buffer, object);
    fieldB.encode(buffer, object);
    fieldC.encode(buffer, object);
    fieldD.encode(buffer, object);
    fieldE.encode(buffer, object);
    fieldF.encode(buffer, object);
  }
}