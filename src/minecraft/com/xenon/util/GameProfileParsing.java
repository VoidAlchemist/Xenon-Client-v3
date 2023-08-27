package com.xenon.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GameProfileParsing {


    public static Optional<Map.Entry<UUID, String>> gameProfileTextureToUUIDAndProfileName(
            GameProfile gameProfile) {
        Property[] props = gameProfile.getProperties().get("textures").toArray(new Property[0]);
        if (props.length != 0) {

            UUID uuid = null;
            String profileName = "";

            try {
                CharBuffer cb = Charsets.UTF_8.newDecoder().decode(
                        ByteBuffer.wrap(
                                Base64.decodeBase64(props[0].getValue())
                        )
                );


                StringBuilder builder = new StringBuilder();

                boolean in_quote = false;
                boolean found_uuid_key = false;
                boolean found_profile_key = false;

                for (int i = 0; i < cb.length(); i++) {
                    char c = cb.get(i);

                    if (c == '"') {

                        if (in_quote) {

                            switch (builder.toString()) {
                                case "profileId":
                                    found_uuid_key = true;
                                    break;
                                case "profileName":
                                    found_profile_key = true;
                                    break;
                                default:
                                    if (found_uuid_key && uuid == null)
                                        uuid = UUIDTypeAdapter.fromString(builder.toString());

                                    else if (found_profile_key && profileName.length() == 0)
                                        profileName = builder.toString();

                                    found_uuid_key = found_profile_key = false;
                                    break;
                            }
                        }

                        in_quote = !in_quote;
                        builder.setLength(0);
                    } else if (in_quote)
                        builder.append(c);

                }

                return Optional.of(new AbstractMap.SimpleEntry<>(uuid, profileName));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }
    

}
