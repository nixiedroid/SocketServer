### Main Ideas

- Do not leave partially-serialized data in object
  All data in object should be readable as is
  For example, fully deserialize flags, do not
  store them inside one int value, use Set<
  Flags_enum> instead

### Useful pakages:

`java.io.Bits` <-- Utility methods for packing/
unpacking primitive values in/ out of byte arrays
using big-endian byte ordering