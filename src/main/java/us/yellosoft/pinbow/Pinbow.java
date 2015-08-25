package us.yellosoft.pinbow;

import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import org.docopt.Docopt;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

public class Pinbow {
  public static final String DOC =
    "Usage:\n" +
    "  pinbow [--cluster=<address>] [--algorithm=<algorithm>] --encrypt=<password>\n" +
    "  pinbow [--cluster=<address>] [--algorithm=<algorithm>] --decrypt=<hash>\n" +
    "  pinbow --version\n" +
    "  pinbow --help\n" +
    "Options:\n" +
    "  -d --decrypt=<hash>         Decrypt a hash.\n" +
    "  -e --encrypt=<password>     Encrypt a password.\n" +
    "  -c --cluster=<address>      Cluster address [default: 127.0.0.1].\n" +
    "  -a --algorithm=<algorithm>  Hash algorithm [default: MD5].\n" +
    "  -v --version                Show version.\n" +
    "  -h --help                   Print usage info.\n";

  public static void put(final Session session, final String password, final String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    final MessageDigest md = MessageDigest.getInstance(algorithm);
    md.update(password.getBytes("UTF-8"));
    final String hash = md.toString();

    session.execute("UPDATE rainbows SET ? ? = ?", algorithm, hash, password);
  }

  public static ResultSet get(final Session session, final String hash, String algorithm) throws NoSuchAlgorithmException {
    return session.execute("SELECT password FROM rainbows WHERE algorithm = ? AND hash = ?", algorithm, hash);
  }

  public static void main(final String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    Map<String, Object> options = new Docopt(DOC).withVersion("0.0.1").parse(args);

    Cluster cluster = Cluster.builder().addContactPoint((String) options.get("--cluster")).build();
    Session session = cluster.connect("rainbows");

    if ((String) options.get("--encrypt") != null) {
      Pinbow.put(session, (String) options.get("--algorithm"), (String) options.get("--encrypt"));
    }
    else if ((String) options.get("--decrypt") != null) {
      System.out.println(Pinbow.get(session, (String) options.get("algorithm"), (String) options.get("--decrypt")));
    }
  }
}
