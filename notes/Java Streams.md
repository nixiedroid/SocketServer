###             

`java.io:`

- `System.in`  The "standard" input stream.
  This stream is already open and ready to supply
  input data. Typically, this stream corresponds
  to
  keyboard input or another input source specified
  by the host environment or user. Can be
  overridden
  InputStream
- `System.out`  The "standard" output stream.
  This stream is already open and ready to accept
  output data. Typically, this stream corresponds
  to display output or another output destination
  specified by the host environment or user.
  PrintStream
- `BufferedOutputStream` The class implements a
  buffered output stream. By setting up such an
  output stream, an application can write bytes to
  the underlying output stream without necessarily
  causing a call to the underlying system for each
  byte written.
- `BufferedInputStream` A BufferedInputStream adds
  functionality to
  another input stream-namely, the ability to
  buffer
  the input and to support the mark and reset
  methods. When the BufferedInputStream is
  created,
  an internal buffer array is created. As bytes
  from
  the stream are read or skipped, the internal
  buffer is refilled as necessary from the
  contained
  input stream, many bytes at a time. The mark
  operation remembers a point in the input stream
  and the reset operation causes all the bytes
  read
  since the most recent mark operation to be
  reread
  before new bytes are taken from the contained
  input stream.
- `Reader` Abstract class for reading character
  streams. The only methods that a subclass must
  implement are read(char[], int, int) and
  close(). Most subclasses, however, will override
  some of the methods defined here in order to
  provide higher efficiency, additional
  functionality, or both. See Also:
  `BufferedReader`, `LineNumberReader`,
  `CharArrayReader`, `InputStreamReader`, `FileReader`,
  `FilterReader`, `PushbackReader`, `PipedReader`,
  `StringReader`, `Writer`
- ByteArrayInputStream A ByteArrayInputStream
  contains an internal buffer that contains bytes
  that may be read from the stream. An internal
  counter keeps track of the next byte to be
  supplied by the read method.
  Closing a ByteArrayInputStream has no effect.
  The methods in this class can be called after
  the stream has been closed without generating an
  IOException.
- `FilterInputStream` A FilterInputStream contains
  some other input stream, which it uses as its
  basic source of data, possibly transforming the
  data along the way or providing additional
  functionality. The class FilterInputStream
  itself simply overrides all methods of
  InputStream with versions that pass all requests
  to the contained input stream. Subclasses of
  FilterInputStream may further override some of
  these methods and may also provide additional
  methods and fields.

