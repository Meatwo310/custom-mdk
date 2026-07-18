package net.meatwo310.examplemod.mdk.config;

public abstract class ConfigInstruction implements ConfigElement {
    public static final class Translation extends ConfigInstruction {
        private final String translationKey;

        public Translation(String translationKey) {
            this.translationKey = translationKey;
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.translation(translationKey);
        }
    }

    public static final class WorldRestart extends ConfigInstruction {
        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.worldRestart();
        }
    }

    public static final class GameRestart extends ConfigInstruction {
        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.gameRestart();
        }
    }

    public static final class Push extends ConfigInstruction {
        private final String key;
        private final String comment;

        public Push(String key, String comment) {
            this.key = key;
            this.comment = comment;
        }

        public String key() {
            return key;
        }

        public String comment() {
            return comment;
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.push(key, comment);
        }
    }

    public static final class Pop extends ConfigInstruction {
        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.pop();
        }
    }

    public static final class PopCount extends ConfigInstruction {
        private final int count;

        public PopCount(int count) {
            this.count = count;
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.pop(count);
        }
    }
}
