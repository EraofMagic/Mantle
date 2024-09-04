package dev.tocraft.eomantle.data.loadable;

import dev.tocraft.eomantle.data.loadable.record.RecordLoadable;

/** Interface for an object that has a loadable. It is expected the loadable returned works on the object itself. */
public interface IAmLoadable {
  /** Loadable instance */
  Loadable<?> loadable();

  /** Interface for an object with a record loadable instance */
  interface Record extends IAmLoadable {
    @Override
    RecordLoadable<?> loadable();
  }
}
