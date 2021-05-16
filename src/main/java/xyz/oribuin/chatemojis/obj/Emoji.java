package xyz.oribuin.chatemojis.obj;

import xyz.oribuin.orilibrary.libs.jetbrains.annotations.NotNull;
import xyz.oribuin.orilibrary.libs.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Emoji {

    @NotNull
    private final String id;
    @NotNull
    private final String name;
    @NotNull
    private final String check;
    @NotNull
    private final String replacement;
    @Nullable
    private UUID creator;

    public Emoji(final String id, final String name, final String check, final String replacement) {
        this.id = id;
        this.name = name;
        this.check = check;
        this.replacement = replacement;
        this.creator = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCheck() {
        return check;
    }

    public String getReplacement() {
        return replacement;
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }


}
