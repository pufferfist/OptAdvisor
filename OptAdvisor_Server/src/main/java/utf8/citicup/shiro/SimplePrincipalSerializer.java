package utf8.citicup.shiro;

import org.apache.shiro.io.SerializationException;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.io.*;
import java.util.Collection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class SimplePrincipalSerializer implements Serializer<PrincipalCollection> {
    private static final int MAGIC = 0x0BADBEEF;

    @Override
    public byte[] serialize(PrincipalCollection principalCollection) throws SerializationException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gout = new GZIPOutputStream(outputStream);
            ObjectOutputStream out = new ObjectOutputStream(gout);

            out.writeInt(MAGIC);
            out.writeShort(principalCollection.getRealmNames().size());

            for (String realm : principalCollection.getRealmNames()) {
                out.writeUTF(realm);
                Collection<?> principals = principalCollection.fromRealm(realm);

                out.writeObject(principals.size());

                for (Object principal : principals)
                    out.writeObject(principal);
            }
            gout.finish();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        }
        return outputStream.toByteArray();
    }

    @Override
    public PrincipalCollection deserialize(byte[] serialized) throws SerializationException {
        ByteArrayInputStream ba = new ByteArrayInputStream(serialized);

        try {
            GZIPInputStream gin = new GZIPInputStream(ba);
            ObjectInputStream in = new ObjectInputStream(gin);
            SimplePrincipalCollection pc = new SimplePrincipalCollection();

            if (in.readInt() != MAGIC)
                throw new SerializationException(
                        "Not valid magic number while deserialize(ing) stored PrincipalCollection - possibly obsolete cookie.");

            int numRealms = in.readShort();

            // realms loop
            for (int i = 0; i < numRealms; i++) {
                String realmName = in.readUTF();

                int numPrincipals = in.readShort();

                // principals loop
                for (int j = 0; j < numPrincipals; j++) {
                    Object principal = in.readObject();

                    pc.add(principal, realmName);
                }
            }

            return pc;
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException(e.getMessage());
        }
    }
}

