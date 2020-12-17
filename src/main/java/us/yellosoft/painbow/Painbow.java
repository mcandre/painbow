package us.yellosoft.painbow;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import org.docopt.Docopt;

import org.apache.commons.codec.binary.Hex;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

/** Cassandra-backed rainbow table */
public final class Painbow {
    /** DocOpt usage spec */
    public static final String DOC = String.join(
    "\n",
    new String[] {
        "Usage:",
        "    painbow [--contact-point=<host>] --migrate",
        "    painbow [--contact-point=<host>] [--algorithm=<algorithm>] --encrypt=<password>",
        "    painbow [--contact-point=<host>] [--algorithm=<algorithm>] --decrypt=<hash>",
        "    painbow [--contact-point=<host>] [--algorithm=<algorithm>] --size",
        "    painbow --version",
        "    painbow --help",
        "Options:",
        "    -m --migrate                                Run migrations.",
        "    -d --decrypt=<hash>                 Decrypt a hash.",
        "    -e --encrypt=<password>         Encrypt a password.",
        "    -s --size                                     Calculate number of passwords stored.",
        "    -c --contact-point=<host>     Cassandra contact point host [default: 127.0.0.1].",
        "    -a --algorithm=<algorithm>    Hash algorithm [default: MD5].",
        "    -v --version                                Show version.",
        "    -h --help                                     Print usage info."
    }
    );

    /** Painbow's Cassandra keyspace */
    public static final String KEYSPACE = "rainbows";

    /** Supported hash algorithms */
    public static final List<String> ALGORITHMS = Collections.unmodifiableList(Arrays.asList("MD2", "MD5", "SHA1", "SHA256", "SHA384", "SHA512"));

    /** Utility class */
    private Painbow() {}
        /**
         * Don't do this. Use a proper migrations framework instead of hardcoding.
         *
         * @param session a Cassandra session
         */
        public static void migrate(final Session session) {
            session.execute(
            String.format(
            "CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 3}",
            KEYSPACE
            )
            );

            for (final var algorithm : ALGORITHMS) {
                session.execute(
                    String.format(
                        "CREATE TABLE IF NOT EXISTS %s.%s (hash text PRIMARY KEY, password text)",
                        KEYSPACE,
                        algorithm
                    )
                );
            }
        }

        /**
         * Inform the rainbow table of a password/hash pair
         *
         * @param session a Cassandra session
         * @param algorithm a supported hash algorithm name
         * @param password a UTF-8 password to store
         *
         * @return a hash string
         * @throws NoSuchAlgorithmException on unsupported algorithm name
         * @throws UnsupportedEncodingException on a poorly encoded password
         */
        public static String put(final Session session, final String algorithm, final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            final var md = MessageDigest.getInstance(algorithm);
            md.update(password.getBytes("UTF-8"));
            final var hash = new String(Hex.encodeHex(md.digest()));

            session.execute(
                String.format(
                    "INSERT INTO %s.%s (hash, password) VALUES (?,?)",
                    KEYSPACE,
                    algorithm.replaceAll("-", "")
                ),
                hash,
                password
            );

            return hash;
        }

        /**
         * Lookup a password from a hash
         *
         * @param session a Cassandra session
         * @param algorithm a supported hash algorith name
         * @param hash a hash string
         *
         * @return a UTF-8 password
         * @throws NoSuchAlgorithmException on unsupported algorithm name
         */
        public static ResultSet get(final Session session, final String algorithm, final String hash) throws NoSuchAlgorithmException {
            return session.execute(
                String.format(
                    "SELECT password FROM %s.%s WHERE hash = ?",
                    KEYSPACE,
                    algorithm.replaceAll("-", "")
                ),
                hash
            );
        }

        /**
         * Count total passwords per algorithm
         *
         * @param session a Cassandra session
         * @param algorithm an algorithm name
         *
         * @return the number of password/hash pairs stored for that algorithm
         */
        public static long size(final Session session, final String algorithm) {
            final var resultSet = session.execute(
                String.format(
                    "SELECT COUNT(*) FROM %s.%s",
                    KEYSPACE,
                    algorithm.replaceAll("-", "")
                )
            );

            final var rows = resultSet.all();

            var total = 0L;

            for (final var row : rows) {
                total += row.getLong("count");
            }

            return total;
        }

        /**
         * CLI entry point
         *
         * @param args CLI flags
         *
         * @throws NoSuchAlgorithmException on an unsupported algorithm name
         * @throws UnsupportedEncodingException on a poorly encoded password
         */
        public static void main(final String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            final var options = new Docopt(DOC).withVersion("0.0.1").parse(args);

            final var cluster = Cluster.builder().addContactPoint((String) options.get("--contact-point")).build();
            final var session = cluster.connect();

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
                final var row = Painbow.get(
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
