package us.yellosoft.painbow;

import java.util.Map;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import org.docopt.Docopt;

import org.apache.commons.codec.binary.Hex;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class Painbow {
  public static final String DOC =
    "Usage:\n" +
    "  painbow [--contact-point=<host>] --migrate\n" +
    "  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --encrypt=<password>\n" +
    "  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --decrypt=<hash>\n" +
    "  painbow [--contact-point=<host>] [--algorithm=<algorithm>] --size\n" +
    "  painbow --version\n" +
    "  painbow --help\n" +
    "Options:\n" +
    "  -m --migrate                Run migrations.\n" +
    "  -d --decrypt=<hash>         Decrypt a hash.\n" +
    "  -e --encrypt=<password>     Encrypt a password.\n" +
    "  -s --size                   Calculate number of passwords stored.\n" +
    "  -c --contact-point=<host>   Cassandra contact point host [default: 127.0.0.1].\n" +
    "  -a --algorithm=<algorithm>  Hash algorithm [default: MD5].\n" +
    "  -v --version                Show version.\n" +
    "  -h --help                   Print usage info.\n";

  public static final String KEYSPACE = "rainbows";

  public static final String[] ALGORITHMS = { "MD2", "MD5", "SHA1", "SHA256", "SHA384", "SHA512" };

  // Don't do this. Use a proper migrations framework instead of hardcoding.
  public static void migrate(final Session session) {
    session.execute(
      "CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE + " WITH REPLICATION = " +
      "{ 'class': 'SimpleStrategy', 'replication_factor': 3}"
    );

    for (String algorithm : ALGORITHMS) {
      session.execute(
        "CREATE TABLE IF NOT EXISTS " + KEYSPACE + "." + algorithm + " " +
        "(hash text PRIMARY KEY, password text)"
      );
    }
  }

  public static String put(final Session session, final String algorithm, final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    final MessageDigest md = MessageDigest.getInstance(algorithm);
    md.update(password.getBytes("UTF-8"));
    final String hash = new String(Hex.encodeHex(md.digest()));

    session.execute(
      "INSERT INTO " + KEYSPACE + "." + algorithm.replaceAll("-", "") + " (hash, password) VALUES (?,?)",
      hash,
      password
    );

    return hash;
  }

  public static ResultSet get(final Session session, final String algorithm, final String hash) throws NoSuchAlgorithmException {
    return session.execute(
      "SELECT password FROM " + KEYSPACE + "." + algorithm.replaceAll("-", "") + " WHERE hash = ?",
      hash
    );
  }

  public static long size(final Session session, final String algorithm) {
    ResultSet resultSet = session.execute(
      "SELECT COUNT(*) FROM " + KEYSPACE + "." + algorithm.replaceAll("-", "")
    );

    List<Row> rows = resultSet.all();

    long total = 0L;

    for (Row row : rows) {
      total += row.getLong("count");
    }

    return total;
  }

  public static void main(final String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    Map<String, Object> options = new Docopt(DOC).withVersion("0.0.1").parse(args);

    Cluster cluster = Cluster.builder().addContactPoint((String) options.get("--contact-point")).build();
    Session session = cluster.connect();

    if ((Boolean) options.get("--migrate")) {
      System.out.println("Migrating...");
      Painbow.migrate(session);
    }
    else if ((String) options.get("--encrypt") != null) {
      System.out.println(
        Painbow.put(
          session,
          (String) options.get("--algorithm"),
          (String) options.get("--encrypt")
        )
      );
    }
    else if ((String) options.get("--decrypt") != null) {
      Row row = Painbow.get(
        session,
        (String) options.get("--algorithm"),
        (String) options.get("--decrypt")
      ).one();

      if (row != null) {
        System.out.println(row.getString("password"));
      }
    }
    else if ((Boolean) options.get("--size") != null) {
      System.out.println(
        size(
          session,
          (String) options.get("--algorithm")
        )
      );
    }

    session.close();
    System.exit(0);
  }
}
