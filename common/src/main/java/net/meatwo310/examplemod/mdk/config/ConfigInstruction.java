package net.meatwo310.examplemod.mdk.config;

public abstract class ConfigInstruction implements ConfigElement {
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
}
