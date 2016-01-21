import com.google.common.collect.Maps;
import kim.ian.chembench.DescriptorSet;
import kim.ian.chembench.ReadDescriptors;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    /**
     * Writes a descriptor matrix to an X file.
     *
     * @param descriptorMatrix - a map of compound names -> a map of descriptor names to descriptor values
     * @param outfile          - a Path to the output X file
     */
    private static void writeToXFile(Map<String, Map<String, Double>> descriptorMatrix, Path outfile) {
        // TODO your code goes here
    }

    public static void main(String[] args) {
        Path exampleDir = Paths.get("examples").toAbsolutePath();
        String sdfName = "aa_48_2.sdf";
        DescriptorSet cdk = ReadDescriptors.read(exampleDir.resolve(sdfName + ".cdk.x"));
        DescriptorSet dragonH = ReadDescriptors.read(exampleDir.resolve(sdfName + ".dragonH"));
        DescriptorSet dragonNoH = ReadDescriptors.read(exampleDir.resolve(sdfName + ".dragonNoH"));
        DescriptorSet ISIDA = ReadDescriptors.read(exampleDir.resolve(sdfName + ".ISIDA"));
        DescriptorSet maccs = ReadDescriptors.read(exampleDir.resolve(sdfName + ".maccs"));
        DescriptorSet moe2d = ReadDescriptors.read(exampleDir.resolve(sdfName + ".moe2D"));

        // compoundName -> [(descriptor1Name, descriptor1Value), ...]
        Map<String, Map<String, Double>> cdkMatrix = cdk.getMatrix();
        Map<String, Map<String, Double>> moe2dMatrix = moe2d.getMatrix();

        // take cdkMatrix and moe2dMatrix and combine them
        Map<String, Map<String, Double>> hybridMatrix = Maps.newTreeMap();
        // TODO your code goes here

        // write out the result to an X file
        String hybridExt = cdk.getDescriptorType().getExtension() + moe2d.getDescriptorType().getExtension() + ".hybrid";
        writeToXFile(hybridMatrix, exampleDir.resolve(sdfName + hybridExt));
    }
}
