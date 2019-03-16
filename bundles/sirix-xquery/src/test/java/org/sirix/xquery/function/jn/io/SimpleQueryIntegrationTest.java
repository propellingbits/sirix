package org.sirix.xquery.function.jn.io;

import java.io.PrintStream;
import org.brackit.xquery.XQuery;
import org.brackit.xquery.util.io.IOUtils;
import org.brackit.xquery.util.serialize.StringSerializer;
import org.brackit.xquery.xdm.Sequence;
import org.junit.Test;
import org.sirix.xquery.SirixCompileChain;
import org.sirix.xquery.SirixQueryContext;
import org.sirix.xquery.json.BasicJsonDBStore;
import junit.framework.TestCase;

public final class SimpleQueryIntegrationTest extends TestCase {

  @Test
  public void test() {
    // Initialize query context and store.
    try (final BasicJsonDBStore store = BasicJsonDBStore.newBuilder().build();
        final SirixQueryContext ctx = SirixQueryContext.createWithJsonStore(store);
        final SirixCompileChain chain = SirixCompileChain.createWithJsonStore(store)) {

      // Use XQuery to store a JSON string into the store.
      System.out.println("Storing document:");
      final String storeQuery = "jn:store('mycol.jn','mydoc.jn','[\"bla\", \"blubb\"]')";
      System.out.println(storeQuery);
      new XQuery(chain, storeQuery).evaluate(ctx);

      // Use XQuery to load a JSON database/resource.
      System.out.println("Opening document again:");
      final String openQuery = "jn:doc('mycol.jn','mydoc.jn')[[0]]";
      System.out.println(openQuery);
      final Sequence seq = new XQuery(chain, openQuery).evaluate(ctx);

      assertNotNull(seq);

      final PrintStream buf = IOUtils.createBuffer();
      new StringSerializer(buf).serialize(seq);
      assertEquals("bla", buf.toString());
    }
  }
}
