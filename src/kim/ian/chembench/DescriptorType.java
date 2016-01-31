package kim.ian.chembench;

public enum DescriptorType {
    CDK(".cdk.x"),
    DragonH(".dragonH"),
    DragonNoH(".dragonNoH"),
    MACCS(".maccs"),
    MOE2D(".moe2D"),
    ISIDA(".ISIDA"),
    HYBRID("");

    private final String extension;

    DescriptorType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
