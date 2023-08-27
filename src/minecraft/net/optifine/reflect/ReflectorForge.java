package net.optifine.reflect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ReflectorForge {
    public static InputStream getOptiFineResourceStream(String path) {
        if (!Reflector.OptiFineClassTransformer_instance.exists())
            return null;
        else {
            Object object = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);

            if (object == null)
                return null;
            else {
                if (path.startsWith("/"))
                    path = path.substring(1);

                byte[] abyte = (byte[]) ((byte[]) Reflector.call(object, Reflector.OptiFineClassTransformer_getOptiFineResource, new Object[]{path}));

                if (abyte == null)
                    return null;
                else {
                    InputStream inputstream = new ByteArrayInputStream(abyte);
                    return inputstream;
                }
            }
        }
    }
}
